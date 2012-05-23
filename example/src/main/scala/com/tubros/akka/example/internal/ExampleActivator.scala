package com.tubros.akka.example.internal

import akka.actor._
import akka.util.duration._
import org.osgi.framework.{
    BundleActivator,
    BundleContext
    }

import com.typesafe.config.ConfigFactory


class ExampleActivator
    extends BundleActivator
{
    /// Class Imports
    import Actor._


    /// Instance Properties
    private var system : Option[ActorSystem] = None;
    private var heartbeat : Option[ActorRef] = None;


    override def start (context : BundleContext) : Unit =
    {
        val confUrl = context.getBundle.getEntry ("/akka-actor.conf");
        val config = ConfigFactory.load (ConfigFactory.parseURL (confUrl));

        system = Some (
            ActorSystem ("OSGi-Example",
                config,
                getClass.getClassLoader
                )
            );
        heartbeat = Some (
            system.get.actorOf (
                Props (new HeartbeatActor (5 seconds)),
                name = "heartbeat"
                )
            );

        heartbeat foreach (_ ! BeginHeartbeat);
    }


    override def stop (context : BundleContext) : Unit =
    {
        system foreach (_.shutdown ());
    }
}

