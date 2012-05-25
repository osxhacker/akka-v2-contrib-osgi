package com.tubros.akka.example.server.internal

import akka.actor._
import org.slf4j.LoggerFactory


class EchoActor
    extends Actor
{
    /// Instance Properties
    private val log = LoggerFactory.getLogger (getClass);


    /// Constructor Body
    log.info ("Echo actor starting up: %s".format (self.path.address));


    override def receive =
    {
        case msg : Any =>
            log.info ("Received [%s]".format (msg));
            sender ! "You said: %s".format (msg);
    }
}
