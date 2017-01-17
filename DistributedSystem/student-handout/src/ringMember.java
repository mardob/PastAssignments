/**
 * ringMember.java
 * interface for ringmemberImpl
 * 2224717
 */

public interface ringMember extends java.rmi.Remote 
{
   /*
    * This allows one ring node object to obtain the circulating token from another.
    */
   public void takeToken(Token item, String fileName) throws java.rmi.RemoteException;
   
}