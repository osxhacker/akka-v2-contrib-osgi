package com.tubros.akka.example.internal


sealed trait HeartbeatEvent
case object BeginHeartbeat
    extends HeartbeatEvent
case object CancelHeartbeat
    extends HeartbeatEvent
case object Heartbeat
    extends HeartbeatEvent
