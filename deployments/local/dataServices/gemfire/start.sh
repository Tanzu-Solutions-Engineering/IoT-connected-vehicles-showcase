cd $GEMFIRE_HOME/bin
$GEMFIRE_HOME/bin/gfsh -e "start locator --name=locator --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1"
$GEMFIRE_HOME/bin/gfsh -e "connect" -e "configure pdx --read-serialized=true --disk-store"
$GEMFIRE_HOME/bin/gfsh -e "start server --name=server1 --locators=localhost[10334] --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1"
$GEMFIRE_HOME/bin/gfsh -e "connect" -e "create region --name=Vehicle --type=PARTITION"
