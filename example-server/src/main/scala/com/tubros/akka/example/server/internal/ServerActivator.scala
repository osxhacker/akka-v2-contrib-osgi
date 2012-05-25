package com.tubros.akka.example.server.internal

import akka.actor._
import org.osgi.framework.{
    BundleActivator,
    BundleContext
    }

import com.typesafe.config.ConfigFactory


class ServerActivator
    extends BundleActivator
{
    /// Class Imports
    import Actor._


    /// Instance Properties
    private var system : Option[ActorSystem] = None;


    override def start (context : BundleContext) : Unit =
    {
        val config = ConfigFactory.load ();

        system = Some (
            ActorSystem ("OSGi-Example-Server", config, getClass.getClassLoader)
            );
        system foreach (_.actorOf (Props[EchoActor], name = "echo"));
    }


    override def stop (context : BundleContext) : Unit =
    {
        system foreach (_.shutdown ());
    }
}
