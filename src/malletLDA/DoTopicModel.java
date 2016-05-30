package malletLDA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequenceWithBigrams;
import cc.mallet.pipe.TokenSequenceNGrams;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;

public class DoTopicModel {

	public DoTopicModel(String folderNameAbs,String stopWordFile,int numberOfIteration
			,int[] sizes, int numTopics, int maxRnak)
	{
		this.folderNameAbs = folderNameAbs;
		this.stopWordFile = stopWordFile;
		this.numberOfIteration = numberOfIteration; // 1500 until 4000
		// tow important factors:
		this.sizes = sizes;
		this.numTopics = numTopics;
		this.maxRnak = maxRnak;

	}
	public static int numTopics = 0;
	public static int maxRnak =0;


	public int[] sizes= null;

	public String stopWordFile = "";
	public int numberOfIteration = 0;

	// give the arabic folder path...
	public String folderNameAbs = "";
	public ArrayList<String> txtcontentlst = new ArrayList<String>();

	public ArrayList<String> run()
	{
		//first read folder content to an arraylist.
		ArrayList<String>  topic_list = null;

		try {
			readFilesToList();
			topic_list = topicModeling(txtcontentlst);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return topic_list;
	}

	public void readFilesToList() throws IOException
	{
		//response.setContentType("text/html");
		//PrintWriter writer = response.getWriter();

		String foldrName =folderNameAbs;
		String folderNameAbs = foldrName;
		//writer.println("Absolute Path: "+ folderNameAbs);
		File node = new File(folderNameAbs);
		//System.out.println(node.getAbsoluteFile());

		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				//writer.println("fileName:" + filename);
				try {
					String cont = readAsingleFile(folderNameAbs + filename);
					this.txtcontentlst.add(cont);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


	public String readAsingleFile(String fileName) throws IOException
	{
		BufferedReader br;
		String cont = "";
		br = new BufferedReader(new FileReader(fileName));
		try {


			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			cont = sb.toString();
			//writer.println(everything);

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			br.close();
		}
		return cont;
	}



	public  ArrayList<String> topicModeling(ArrayList<String> docsFileContentArrayList) throws Exception {
		//String pattern_str = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		//Pattern pattern = Pattern.compile(pattern_str);
		//java.util.regex.Matcher matcher=null;

		ArrayList<String> alltopicset = new ArrayList<String>();
		int[] sizes = this.sizes;
		int numTopics = this.numTopics;
		int maxRank = this.maxRnak;
		int numberOfIteration = this.numberOfIteration;
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		pipeList.add( new CharSequenceLowercase() );
		pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
		//Hungarain is for the liberal, but Slovakian is for the radical "majorriy".
		//String stopWordsFile = selectedStopwords==1?opt.StopWordsHung: opt.StopWordsSlovak;
		String stopWordsFile = this.stopWordFile;
		pipeList.add( new TokenSequenceRemoveStopwords(new File(stopWordsFile), "UTF-8", false, false, false) );
		pipeList.add(new TokenSequenceNGrams(sizes));
		pipeList.add( new TokenSequence2FeatureSequenceWithBigrams() );
		InstanceList instances = new InstanceList (new SerialPipes(pipeList));

		int c=0;
		for(String filecontent:docsFileContentArrayList)
		{
			String fileId = new Integer(c).toString();
			//filecontent = filecontent.replaceAll(pattern_str,"");
			Instance an_instance = new Instance(filecontent, fileId, null, "X");
			instances.addThruPipe(an_instance);
			++c;
		}

		ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);
		model.addInstances(instances);
		model.setNumThreads(4);
		model.setNumIterations(numberOfIteration);
		model.estimate();


		model.estimate();


		Alphabet dataAlphabet = instances.getDataAlphabet();

		FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
		LabelSequence topics = model.getData().get(0).topicSequence;

		Formatter out = new Formatter(new StringBuilder(), Locale.US);
		for (int position = 0; position < tokens.getLength(); position++) {
			out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
		}
		System.out.println(out);

		// Estimate the topic distribution of the first instance, 
		//  given the current Gibbs state.
		double[] topicDistribution = model.getTopicProbabilities(0);

		// Get an array of sorted sets of word ID/count pairs
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

		// Show top 5 words in topics with proportions for the first document
		for (int topic = 0; topic < numTopics; topic++) {
			Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

			out = new Formatter(new StringBuilder(), Locale.US);
			out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
			int rank = 0;
			while (iterator.hasNext() && rank < 25) {
				IDSorter idCountPair = iterator.next();
				out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
				alltopicset.add(dataAlphabet.lookupObject(idCountPair.getID())+": Topic: " +
						+topic+", Keyword number: "+ rank+"");
				rank++;
			}
			System.out.println(out);
		}

		// Create a new instance with high probability of topic 0
		StringBuilder topicZeroText = new StringBuilder();
		Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

		int rank = 0;
		while (iterator.hasNext() && rank < 25) {
			IDSorter idCountPair = iterator.next();
			topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
			rank++;
		}

		// Create a new instance named "test instance" with empty target and source fields.
		InstanceList testing = new InstanceList(instances.getPipe());
		testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

		TopicInferencer inferencer = model.getInferencer();
		double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
		System.out.println("0\t" + testProbabilities[0]);
		
		return alltopicset;

	}
}
