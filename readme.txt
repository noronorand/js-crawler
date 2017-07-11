I have tried to skip any 3rd party library (except for logging) so there are several hacks to get it in in reasonable time:

1. Google search 
	- uses browser webagent (rather than search API). Extensive searches will be detected as "unusual traffic" by google, so I have disabled unit tests (it is safer than to rely on skipTests)
2. HtmlParser
	- I would pick up some 3rd party library, but here i rely on my limited regex skills. Probably some issues with it and it is missing good unit test working on predownloaded data.
3. Runner
	- I have used ExecutorService with up to 10 threads and timeout on whole batch. Implementing timeout on single task will be better, but i think this is ok for scope of this exercise.
4. JS and duplicates
	- this one does not detect one. Maybe to massage version numbers or download js script itself and calculate checksum. Not sure what to do with obfuscated libs.
	
To run app:

java tj.scalable.Start searchterm1 searcherm2 ....

	
