package state_checkPoint

import java.util.concurrent.TimeUnit

import bean.SensorReading
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.time.Time
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.runtime.state.memory.MemoryStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala._

/**
 * @Auther: wxf
 * @Date: 2022/7/19 19:08:42
 * @Description: CheckPointTest
 * @Version 1.0.0
 */
object CheckPointTest {
  def main(args: Array[String]): Unit = {
    //生成配置对象
    val conf: Configuration = new Configuration()
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    // web 查看 http://localhost:8081/
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)
    env.setParallelism(1)

    //    val input: DataStream[String] = env.socketTextStream("localhost", 6666)

    // checkPoint 相关配置
    val checkPointDir: String = ""
    // 开启checkPoint   指定检查点间隔时间(毫秒)
    env.enableCheckpointing(1000L)
    // 其他配置
    // 设置检查点模式  默认 EXACTLY_ONCE(只有一次)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    // 超时时间 超过这个时间那么将 checkPoint 丢弃
    env.getCheckpointConfig.setCheckpointTimeout(30000L)
    // 最多同时有两个checkPoint进行存盘
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(2)
    // 两次checkPoint之间至少留下多少时间 处理数据
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500L)
    // 任务失败 使用当前最近一次的checkPoint恢复(默认 true),还是从savePoint恢复(false)
    env.getCheckpointConfig.setPreferCheckpointForRecovery(false)
    // 允许当前checkPoint 失败几次
    env.getCheckpointConfig.setTolerableCheckpointFailureNumber(3)

    // checkPoint 重启策略
    // 按照 固定延迟重启
    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 10000L))
    // 按照 失败率(故障率)重启
    env.setRestartStrategy(RestartStrategies.failureRateRestart(5, Time.of(5, TimeUnit.MINUTES), Time.of(2, TimeUnit.SECONDS)))

    // 设置 状态后端   -- flink默认 内存级别状态后端
    // 使用内存级 状态后端
    env.setStateBackend(new MemoryStateBackend())
    // 使用外部文件系统 状态后端
    env.setStateBackend(new FsStateBackend(checkPointDir))
    // 使用RocksDB 状态后端  true设置增量保存checkPoint
    env.setStateBackend(new RocksDBStateBackend(checkPointDir, true))

    // 设置 生成检查点 的间隔周期
    env.enableCheckpointing(100)
    // 配置重启策略   重启60次，每次间隔10秒
    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(60, Time.of(10, TimeUnit.SECONDS)))

  }
}