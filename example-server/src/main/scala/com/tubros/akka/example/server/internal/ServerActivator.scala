package com.tubros.akka.example.server.internal

import akka.actor._
import org.osgi.framework.{
    BundleActivator,
    BundleContext
    }

import com.typesafe.config.{
    Config,
    ConfigFactory
    }


class ServerActivator
    extends BundleActivator
{
    /// Class Imports
    import Actor._


    /// Instance Properties
    private var system : Option[ActorSystem] = None;


    override def start (context : BundleContext) : Unit =
    {
        val config = loadConfiguration (context);

        system = Some (
            ActorSystem ("OSGi-Example-Server", config, getClass.getClassLoader)
            );
        system foreach (_.actorOf (Props[EchoActor], name = "echo"));
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
