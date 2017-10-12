# Run OdmHadoop adaptor in embedded mode.  Use HBase as the persistence layer

# Remove output folder
hdfs dfs -rm output/*
hdfs dfs -rmdir output

HBASE_HOME=/usr/iop/4.2.0.0/hbase
HBASE_LIBS=`(hbase mapredcp | tr ':' ',')`
HBASE_CLASSPATH=${HBASE_HOME}/conf,${HBASE_LIBS}

echo "HBASE PATH:"
echo ${HBASE_CLASSPATH}

# Run the Hadoop job
#yarn jar  odmhbasehadoop.jar mapreduce.OdmHadoopAdaptor \
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

yarn jar odmhadoop.jar mapreduce.OdmHadoopAdaptor -libjars ${HBASE_CLASSPATH} \
             input \
             output \
             passportNumber,customerName,dateOfBirth,flightNumber,flightDate,route \
             /validatePnrApp/1.0/validatePnr/1.0 \
             brsv2-7a461af4.ng.bluemix.net \
             resAdmin \
             1hv0ftlm0j9ys \
             "Basic cmVzQWRtaW46MWh2MGZ0bG0wajl5cw=="\
             false \
             true

# Display the result
echo "Result:"
echo ""
hdfs dfs -cat output/*
