call mvn install:install-file -DgroupId=kxml2 -DartifactId=kxml2 -Dversion=2.3.0 -Dpackaging=jar -Dfile=lib\kxml2-2.3.0.jar -DgeneratePom=true
call mvn install:install-file -DgroupId=org.helyx -DartifactId=json4me -Dversion=1.0.0 -Dpackaging=jar -Dfile=lib\json4me-1.0.0.jar -DgeneratePom=true

pause
