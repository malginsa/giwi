#!/bin/sh
echo "BEFORE"
ls -l ~msa/msa_jstudy/GwtGiwi/war/WEB-INF/classes/
cp ~msa/giwi_project/log4j2.xml ~msa/msa_jstudy/GwtGiwi/war/WEB-INF/classes/
cp ~msa/giwi_project/config.xml ~msa/msa_jstudy/GwtGiwi/war/WEB-INF/classes/
cp ~msa/giwi_project/db.properties ~msa/msa_jstudy/GwtGiwi/war/WEB-INF/classes/
echo "AFTER"
ls -l ~msa/msa_jstudy/GwtGiwi/war/WEB-INF/classes/
