package com.zo

import com.typesafe.config._

trait BaseConfig {
    
    protected val conf: Config = ConfigFactory.load()
}

trait RedisConfig extends BaseConfig {
    
    private val redisConfig = conf.getConfig("redis")
    
    val redisHost: String = redisConfig.getString("host")
    val redisPort: Int = redisConfig.getInt("port")
}
