package com.tubros.akka.example.internal

import akka.actor._
import akka.util._
import akka.util.duration._
import org.slf4j.LoggerFactory


class HeartbeatActor (
    val frequency : Duration
    )
    extends Actor
{
    /// Class Imports
    import scala.compat.Platform._


    /// Instance Properties
    private val log = LoggerFactory.getLogger (getClass);
    private var task : Option[Cancellable] = None;


    override def receive =
    {
        case BeginHeartbeat =>
            log.error ("Staring the heartbeat");
            task = Some (
                context.system.scheduler.schedule (0 milliseconds,
                    frequency,
                    self,
                    Heartbeat
                    )
                );

        case Heartbeat =>
            log.error ("Heartbeat: %tT".format (currentTime));

        case CancelHeartbeat =>
            log.error ("Stopping the heartbeat");
            task foreach (_.cancel ());
    }
}
