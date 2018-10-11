@echo off
if not exist "build\modules" echo Module directory build\modules does not exist! Did you follow the README.md instructions? && exit /B
echo Working...
>build\buildloc.tmp where build\modules:jaudiotagger*.jar
jar uf @build\buildloc.tmp -C build\injections_info\jaudiotagger module-info.class
>build\buildloc.tmp where build\modules:jorbis*.jar
jar uf @build\buildloc.tmp -C build\injections_info\jorbis module-info.class
>build\buildloc.tmp where build\modules:tritonus-share*.jar
jar uf @build\buildloc.tmp -C build\injections_info\tritonus-share module-info.class
>build\buildloc.tmp where build\modules:vorbisspi*.jar
jar uf @build\buildloc.tmp -C build\injections_info\vorbisspi module-info.class
del build\buildloc.tmp
javapackager -deploy -native image -name IRiina -outdir build\win -outfile IRiina --module-path build\modules --add-modules org.glassfish.java.json,vorbisspi -v --module net.cyriaca.riina.misc.iriina/net.cyriaca.riina.misc.iriina.beatmapper.IRiina -title IRiina -vendor "Cyriaca Technologies" -description "IRiina Intralism Editor" -Bicon=build\components\icon_win.ico -BappVersion=1.0 -Bidentifier=net.cyriaca.riina.misc.iriina