/*
 * Generated by XDoclet - Do not edit!
 */
package it.iccu.sbn.ejb.domain.stampe;

/**
 * Home interface for Comunicazione.
 * @generated
 * @wtp generated
 */
public interface SBNStampeComunicazioneHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/SBNStampeComunicazioneOnLine";
   public static final String JNDI_NAME="sbnWeb/SBNStampeComunicazioneOnLine";

   public SBNStampeComunicazione create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}