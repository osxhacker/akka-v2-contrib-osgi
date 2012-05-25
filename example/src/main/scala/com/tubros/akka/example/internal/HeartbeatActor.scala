package com.tubros.akka.example.internal

import akka.actor._
import akka.pattern.ask
import akka.util._
import akka.util.duration._
import org.slf4j.LoggerFactory


class HeartbeatActor (
    val frequency : Duration,
    val echoServerUri : String
    )
    extends Actor
{
    /// Class Imports
    import scala.compat.Platform._


    /// Instance Properties
    implicit val timeout = Timeout (5 seconds);
    private val log = LoggerFactory.getLogger (getClass);
    private var echo : Option[ActorRef] = None;
    private var task : Option[Cancellable] = None;


    override def receive =
    {
        case BeginHeartbeat =>
            log.info ("Staring the heartbeat");
            task = Some (
                context.system.scheduler.schedule (0 milliseconds,
                    frequency,
                    self,
                    Heartbeat
                    )
                );

            log.info ("Resolving Echo server");
            echo = Some (context.actorFor (echoServerUri));

        case Heartbeat =>
            log.info ("Heartbeat: %tT".format (currentTime));

            echo foreach {
                remote =>

                (remote ? currentTime) foreach {
                    response =>

                    log.info ("Calling echo: %s".format (response));
                    }
                }

        case CancelHeartbeat =>
            log.info ("Stopping the heartbeat");
            task foreach (_.cancel ());
    }
}
