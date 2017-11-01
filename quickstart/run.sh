# Run OdmHadoop adaptor

# Remove output folder
hdfs dfs -rm output/*
hdfs dfs -rmdir output

# Run the Hadoop job
#yarn jar  odmhadoop.jar mapreduce.OdmHadoopAdaptor \
#             inputDir [hdfs input directory]
#             outputDir [hdfs output directory]
#             rulesetVersion [ruleset path]
#             host [host name of Res]
#             resUser [Res user name]
#             resPwd [Res password]
#             resExPwd [Res exec password ]
#             blueMixMode [true|false]
#             https [true|false]

yarn jar  odmhadoop.jar mapreduce.OdmHadoopAdaptor \
             input \
             output \
             /PNRCheckRuleApp/1.0/PNRCheck/1.0 \
             brsv2-b6dcdca2.ng.bluemix.net \
             resAdmin \
             u7ralgw4gqwm \
             "Basic cmVzQWRtaW46dTdyYWxndzRncXdt" \
             true \
             true

# Display the result
echo "Result:"
echo ""
hdfs dfs -cat output/*
