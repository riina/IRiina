#!/usr/bin/env bash
if [ ! -d "build/modules" ]
then
  echo Module directory build/modules does not exist! Did you follow the README.md instructions?
  exit 1
fi
echo Working...
jar uf "$(find build/modules -maxdepth 1 -type f -name jaudiotagger*.jar)" -C build/injections_info/jaudiotagger module-info.class
jar uf "$(find build/modules -maxdepth 1 -type f -name jorbis*.jar)" -C build/injections_info/jorbis module-info.class
jar uf "$(find build/modules -maxdepth 1 -type f -name tritonus-share*.jar)" -C build/injections_info/tritonus-share module-info.class
jar uf "$(find build/modules -maxdepth 1 -type f -name vorbisspi*.jar)" -C build/injections_info/vorbisspi module-info.class
javapackager -deploy -native image -name IRiina -outdir build/mac -outfile IRiina --module-path build/modules --add-modules org.glassfish.java.json,vorbisspi -v --module net.cyriaca.riina.misc.iriina/net.cyriaca.riina.misc.iriina.beatmapper.IRiina -title IRiina -vendor "Cyriaca Technologies" -description "IRiina Intralism Editor" -Bicon=build/components/icon_mac.icns -BappVersion=1.0.1 -Bidentifier=net.cyriaca.riina.misc.iriina
