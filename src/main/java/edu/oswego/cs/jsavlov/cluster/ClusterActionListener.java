package edu.oswego.cs.jsavlov.cluster;

/**
 * Created by jason on 5/11/16.
 */
public interface ClusterActionListener
{
    void onReceiveNewMessage(String message);
}
