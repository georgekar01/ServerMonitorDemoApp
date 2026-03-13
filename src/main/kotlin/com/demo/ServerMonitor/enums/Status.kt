package com.demo.ServerMonitor.enums

import com.fasterxml.jackson.annotation.JsonProperty

enum class Status {
    //@JsonProperty("UP")
    UP,

    //@JsonProperty("DOWN")
    DOWN,

    //@JsonProperty("DEGRADED")
    DEGRADED
}