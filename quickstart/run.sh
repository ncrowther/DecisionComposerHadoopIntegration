# Run OdmHadoop adaptor

# Remove output folder
hdfs dfs -rm output/*
hdfs dfs -rmdir output

# Run the Hadoop job
#yarn jar  odmhadoop.jar mapreduce.OdmHadoopAdaptor \
#             inputDir [hdfs input directory]
#             outputDir [hdfs output directory]
#             column.names  [comma separated list of columns of csv data]
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
             passportNumber,customerName,dateOfBirth,flightNumber,flightDate,route \
             /validatePnrApp/1.0/validatePnr/1.0 \
             brsv2-ae05805f.ng.bluemix.net \
             resAdmin \
             skn9o0rwqng2 \
             "Basic cmVzQWRtaW46c2tuOW8wcndxbmcy" \
             true \
             true

# Display the result
echo "Result:"
echo ""
hdfs dfs -cat output/*
