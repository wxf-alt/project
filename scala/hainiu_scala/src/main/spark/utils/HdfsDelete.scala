package utils

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

class HdfsDelete(val path:String) {
  def deletePath():Unit = {
    val conf: Configuration = new Configuration
    // 设置本地文件系统
    conf.set("fs.defaultFS", "file:///")
    val fs: FileSystem = FileSystem.get(conf)
    val outputPath: Path = new Path(path)

    if(fs.exists(outputPath)){
      fs.delete(outputPath,true)
      println(s"delete outputPath:${path} success")
    }
  }
}
