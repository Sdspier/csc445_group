csc445_group

Step 1: Leader Election

Step 2: Have servers store data

Step 3: Create clients that will send data to servers

Step 4: Run text editor on clients

Step 5: Have clients send data to servers

Step 6: Have the leader server do log replication

##############################################################################################################################


##_ AtomixReplica (Server) _##

When the replica is started, it will attempt to contact members in the configured startup Address list. 

If any of the members are already in an active state, the replica will request to join the cluster. 

During the process of joining the cluster, the replica will notify the current cluster leader of its existence. If the leader already knows about the joining replica, the replica will immediately join and become a full voting member. 

If the joining replica is not yet known to the rest of the cluster, it will join the cluster in a passive state in which it receives replicated state from other replicas in the cluster but does not participate in elections or other quorum-based aspects of the underlying consensus algorithm. 

Once the joining replica is caught up with the rest of the cluster, the leader will promote it to a full voting member.
 
Once the replica has joined the cluster, it will persist the updated cluster configuration to disk via the replica's      configured Storage module. This is important to note as in the event that the replica crashes, the replica will recover from its last known configuration rather than the configuration provided to the builder factory. 

This allows Atomix cluster structures to change transparently and independently of the code that configures any given replica. If a persistent Storage Level is used, user code should simply configure the replica consistently based on the initial replica configuration, and the replica will recover from the last known cluster configuration in the event of a failure.




 
