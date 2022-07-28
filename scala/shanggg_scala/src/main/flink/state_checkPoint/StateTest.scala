package state_checkPoint

import java.util
import java.util.concurrent.TimeUnit

import bean.SensorReading
import org.apache.flink.api.common.functions.{RichFlatMapFunction, RichMapFunction}
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.api.common.time.Time
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.runtime.state.memory.MemoryStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
 * @Auther: wxf
 * @Date: 2022/7/12 17:26:25
 * @Description: StateTest  监测连续两次温度 差值达到 10 报警
 * @Version 1.0.0
 */
object StateTest {
  def main(args: Array[String]): Unit = {
    //生成配置对象
    val conf: Configuration = new Configuration()
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    // web 查看 http://localhost:8081/
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)
    env.setParallelism(1)

    //    // 设置 状态后端   -- flink默认 内存级别状态后端
    //    // 使用内存级 状态后端
    //    env.setStateBackend(new MemoryStateBackend())
    //    // 使用外部文件系统 状态后端
//        env.setStateBackend(new FsStateBackend("file:\\E:\\A_data\\4.测试数据\\flink-checkPoint\\StateTest"))
    //    // 使用RocksDB 状态后端 设置增量保存checkPoint
    //    env.setStateBackend(new RocksDBStateBackend("checkpoint地址", true))
    //
    //    // 设置 生成检查点 的间隔周期
    //    env.enableCheckpointing(100)
    //    // 配置重启策略   重启60次，每次间隔10秒
    //    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(60,Time.of(10, TimeUnit.SECONDS)))

    val input: DataStream[String] = env.socketTextStream("localhost", 6666)
    val mapStream: DataStream[SensorReading] = input.map(x => {
      val str: Array[String] = x.split(" ")
      SensorReading(str(0), str(1).toLong, str(2).toDouble)
    })

    val result: DataStream[(String, Double, Double)] = mapStream
      .keyBy("id")
      .flatMapWithState[(String, Double, Double), Double]({
        case (inputData: SensorReading, None) => (List.empty, Some(inputData.temperature))
        case (inputData: SensorReading, lastTemp: Some[Double]) => {
          val diss: Double = (inputData.temperature - lastTemp.get).abs
          if (diss > 10.0) {
            (List((inputData.id, lastTemp.get, inputData.temperature)), Some(inputData.temperature))
          } else {
            (List.empty, Some(inputData.temperature))
          }
        }
      })

    //    // 调用 RichMapFunction
    //    // 使用 Keyed State,必须在 KeyBy 之后的操作中(基于一个 KeyedStream)
    //    val result: DataStream[(String, Double, Double)] = mapStream.map(new MyMapFunction(10D))
    //    val result: DataStream[(String, Double, Double)] = mapStream.keyBy("id").map(new MyMapFunction(10D))

    //    // 调用 RichFlatMapFunction
    //    val result: DataStream[(String, Double, Double)] = mapStream.keyBy("id").flatMap(new TemperatureAlertFunction(10D))

    result.print("result:")

    env.execute("StateTest")
  }
}

// 自定义 keyed state 键控状态  基于一个 KeyedStream使用
class MyMapFunction(threshold: Double) extends RichMapFunction[SensorReading, (String, Double, Double)] {

  // 定义状态变量
  var lastTempState: ValueState[Double] = _

  override def open(parameters: Configuration): Unit = {
    lastTempState = getRuntimeContext.getState(new ValueStateDescriptor[Double]("last-temp", classOf[Double], -123))
  }

  override def map(value: SensorReading) = {
    // 获取状态中存储的上一次状态值
    val lastTemp: Double = lastTempState.value()
    // 更新状态
    lastTempState.update(value.temperature)

    // 与当前温度比较
    val diff: Double = (value.temperature - lastTemp).abs
    if (diff > threshold && lastTemp != -123) {
      (value.id, lastTemp, value.temperature)
    } else {
      (value.id, 0.0, 0.0)
    }
  }

}

// 自定义 keyed state 键控状态  基于一个 KeyedStream使用
class TemperatureAlertFunction(val threshold: Double) extends RichFlatMapFunction[SensorReading, (String, Double, Double)] {

  private var lastTempState: ValueState[Double] = _

  override def open(parameters: Configuration): Unit = {
    val lastTempDescriptor = new ValueStateDescriptor[Double]("lastTemp", classOf[Double], -123)
    lastTempState = getRuntimeContext.getState[Double](lastTempDescriptor)
  }

  override def flatMap(reading: SensorReading, out: Collector[(String, Double, Double)]): Unit = {
    val lastTemp = lastTempState.value()
    val tempDiff = (reading.temperature - lastTemp).abs
    if (tempDiff > threshold && lastTemp != -123) {
      out.collect((reading.id, reading.temperature, lastTemp))
    }
    this.lastTempState.update(reading.temperature)
  }
}

// 自定义 operator state 算子状态
class MyOperatorState extends RichMapFunction[SensorReading, Long] with ListCheckpointed[java.lang.Long] {

  var count: Long = 0L

  override def map(value: SensorReading): Long = {
    count += 1
    count
  }

  // 拿出状态
  override def restoreState(state: util.List[java.lang.Long]): Unit = {
//    val iter: util.Iterator[Long] = state.iterator()
//    while(iter.hasNext){
//      count += iter.next()
//    }
    import scala.collection.convert.wrapAll._
    for(countState <- state){
      count += countState
    }
  }

  // 快照,存储状态
  override def snapshotState(checkpointId: Long, timestamp: Long): util.List[java.lang.Long] = {
    val stateList: util.ArrayList[java.lang.Long] = new util.ArrayList[java.lang.Long]()
    stateList.add(count)
    stateList
  }

}