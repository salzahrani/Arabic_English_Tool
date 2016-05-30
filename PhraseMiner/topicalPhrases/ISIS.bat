@echo off
echo %cd%
cd PhraseMiner/topicalPhrases
echo 'changing fodler'
echo %cd%
@set inputFile=C:\Users\Sultan\IdeaProjects\JavaArabic\PhraseMiner\topicalPhrases\rawFiles\ISIS.txt
@set minsup=10
@set thresh=6
@set maxPattern=5
@set gibbsSamplingIterations=2000
@set topicModel=6
echo 'Sultan'
echo %cd%
cd TopicalPhrases
echo %cd%
echo Data preparing...
call win_runDataPreparation.bat %inputFile%

echo Continuous Pattern Mining ...
call win_runCPM.bat %minsup% %maxPattern% %thresh%

set numTopics=6
set optimizationBurnIn=100
set alpha=2
set optimizationInterval=100
call win_runPhrLDA.bat %topicModel% %numTopics% %gibbsSamplingIterations% %optimizationBurnIn% %alpha% %optimizationInterval%

call win_createUnStem.bat %inputFile% %maxPattern%
python unMapper.py input_dataset\input_vocFile input_dataset\input_stemMapping input_dataset_output\unmapped_phrases input_dataset_output\input_partitionedTraining.txt input_dataset_output\newPartition.txt

copy input_dataset_output\newPartition.txt ..\output\corpus.txt
copy input_dataset_output\input_wordTopicAssign.txt ..\output\topics.txt
rem rmdir input_dataset /s /q
rem rmdir input_dataset_output /s /q

cd ..
cd output
python topPhrases.py
python topTopics.py
move *.txt outputFiles
cd ..
exit 0
