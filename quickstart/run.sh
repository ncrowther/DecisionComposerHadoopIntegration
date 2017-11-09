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
#             cloud mode [true|false]
#             https [true|false]

yarn jar  odmhadoop.jar mapreduce.OdmHadoopAdaptor \
             input \
             output \
             /PNRCheckRuleApp/1.0/PNRCheck/1.0 \
             brsv2-cc7fcf8f.ng.bluemix.net \
             resAdmin \
             1hrbg76fu9j6p \
             "Basic cmVzQWRtaW46MWhyYmc3NmZ1OWo2cA==" \
             true \
             true

# Display the result
echo "Result:"
echo ""
hdfs dfs -cat output/*
