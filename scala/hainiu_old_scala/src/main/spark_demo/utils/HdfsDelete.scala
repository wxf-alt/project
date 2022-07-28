package utils

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

// 删除输出目录方法
class HdfsDelete(val path:String) {
  def deletePath:Unit = {
    val conf = new Configuration
    val fs: FileSystem = FileSystem.get(conf)
    val outputPath = new Path(path)

    if(fs.exists(outputPath)){
      fs.delete(outputPath,true)
      println(s"delete outputPath:${path} success")
    }
  }
}