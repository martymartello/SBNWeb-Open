/*
 * Generated by XDoclet - Do not edit!
 */
package it.iccu.sbn.ejb.domain.stampe;

/**
 * Home interface for Etichette.
 * @generated
 * @wtp generated
 */
public interface SBNStampeEtichetteHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/SBNStampeEtichetteOnLine";
   public static final String JNDI_NAME="sbnWeb/SBNStampeEtichetteOnLine";

   public SBNStampeEtichette create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}
