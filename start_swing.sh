#!/bin/bash
pkill -f AvCarSwingApp 2>/dev/null
sleep 1

if [ ! -f /tmp/av_cp.txt ]; then
  mvn dependency:build-classpath -q -DincludeScope=runtime -Dmdep.outputFile=/tmp/av_cp.txt
fi

CP="/home/ramon/Documentos/av_car/target/classes:$(cat /tmp/av_cp.txt)"

nohup bash -c "DISPLAY=:0 java -cp \"$CP\" br.edu.senai.fatesg.avcar.swing.AvCarSwingApp" \
  > /tmp/avcar_swing.log 2>&1 &
echo $! > /tmp/avcar_pid.txt

for i in $(seq 1 20); do
  sleep 0.5
  WID=$(DISPLAY=:0 wmctrl -l 2>/dev/null | grep "AV-CAR Auto Center" | awk '{print $1}' | head -1)
  if [ -n "$WID" ]; then
    DISPLAY=:0 wmctrl -i -R "$WID"
    echo "OK — janela $WID (PID $(cat /tmp/avcar_pid.txt))"
    exit 0
  fi
done
echo "Timeout. Verifique /tmp/avcar_swing.log"
