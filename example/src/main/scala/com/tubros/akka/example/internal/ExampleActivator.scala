package com.tubros.akka.example.internal

import akka.actor._
import akka.util.duration._
import org.osgi.framework.{
    BundleActivator,
    BundleContext
    }

import com.typesafe.config.{
    Config,
    ConfigFactory
    }


class ExampleActivator
    extends BundleActivator
{
    /// Class Imports
    import Actor._


    /// Instance Properties
    val echoServerKey = "osgiexample.server.echo";
    val frequencyKey = "osgiexample.frequency";
    private var system : Option[ActorSystem] = None;
    private var heartbeat : Option[ActorRef] = None;


    override def start (context : BundleContext) : Unit =
    {
        val config = loadConfiguration (context);

        system = Some (
            ActorSystem ("OSGi-Example",
                config,
                getClass.getClassLoader
                )
            );
        heartbeat = Some (
            system.get.actorOf (
                Props (
                    new HeartbeatActor (
                        config.getInt (frequencyKey) seconds,
                        config.getString (echoServerKey)
                        )
                    ),
                name = "heartbeat"
                )
            );

        heartbeat foreach (_ ! BeginHeartbeat);
    }


    override def stop (context : BundleContext) : Unit =
    {
        system foreach (_.shutdown ());
    }


    private def loadConfiguration (context : BundleContext) : Config =
    {
        // This would be resolved by other means in a real project
        val configs = List (
            "/akka/actor/reference.conf",
            "/akka/remote/reference.conf",
            "/application.conf"
            ) map (context.getBundle.getEntry) map {
                url => 

                ConfigFactory.load (ConfigFactory.parseURL (url));
            };

        return (configs.reduceLeft ((b, a) => a.withFallback (b)));
    }
}

