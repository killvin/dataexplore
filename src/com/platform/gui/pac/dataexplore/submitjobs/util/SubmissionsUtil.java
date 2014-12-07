package com.platform.gui.pac.dataexplore.submitjobs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.gui.pac.dataexplore.submitjobs.model.DataFile;
import com.platform.gui.pac.dataexplore.submitjobs.model.InputFile;
import com.platform.gui.pac.dataexplore.submitjobs.model.SubmissionForm;

public class SubmissionsUtil {
	
	public static void sortSubmissionForms(List<SubmissionForm> submissionForms){
		//check the submission form's weight
		//and its weight value come from the below factor values
		//0. by default, the submission form's weight value is 1
		//1. if it be marked as favorite, its weight value + 2
		//2. added the data file's weight value
		//3. check the data file's limit if the submission form's weight value is be same.
		Collections.sort(submissionForms, new Comparator<SubmissionForm>(){
			@Override
			public int compare(SubmissionForm s1, SubmissionForm s2) {
				Integer w1 = s1.getWeight();
				Integer w2 = s2.getWeight();
				
				//TODO, check the condition for the #3
				return w1 - w2;
			}
		});
	}
	
	
	public static Map<DataFile, List<InputFile>> fileMappingDataFiles(List<DataFile> dataFiles, List<InputFile> inputFiles){
		Map<DataFile, List<InputFile>> r = new HashMap<DataFile, List<InputFile>>();
		//the simply way is to mapping the file to data file for 1:1, like these
		/*
		for(int i=0;i<inputFiles.size();i++){
			List<InputFile> fs = new ArrayList<InputFile>();
			fs.add(inputFiles.get(i));
			r.put(dataFiles.get(i), fs);
		}
		*/
		//but it ignore an import fact, the data file has its own weight. So, the files mapping should 
		//be with this order. 
		//by default, all of the data file's weight value is 1.
		//if a data file is required, its weight value should be + 2
		//if a data file is required, but support may files, its weight value is less than the data file which 
		//only support one file. 
		//for example, 
		//if a data file(#A) is required, so its weight value is 3. The other data file(#B) is also required
		//its weight value is also 3, but its site in the list is more front than #A.
		
		//sort the data file list at first with its weight, and site position.
		Collections.sort(dataFiles, new Comparator<DataFile>(){
			@Override
			public int compare(DataFile f0, DataFile f1) {
				Integer w0 = f0.getWeight();
				Integer w1 = f1.getWeight();
				
				if(w0 != w1){
					return w0 - w1;
				}else{
					//check its limit value
					Integer limit0 = f0.getLimit();
					Integer limit1 = f1.getLimit();
					
					return -1*(limit0 - limit1);
				}
			}
		});
		
		for(DataFile dataFile : dataFiles){
			Integer limit = dataFile.getLimit();
			List<InputFile> fs = getArrangeInputFiles(inputFiles, limit);
			r.put(dataFile, fs);
		}
		
		return r;
	}
	
	private static Integer index = 0;
	public static List<InputFile> getArrangeInputFiles(List<InputFile> inputFiles, Integer arrange){
		List<InputFile> r = new ArrayList<InputFile>();
		Integer size = inputFiles.size();
		
		Integer from = index;
		if(from >= size) from = size;
		
		Integer end = size;
		if(arrange != -1){
			end = from + arrange;
			if(end >= size) end = size;
		}
		
		for(int i=from; i<end;i++){
			r.add(inputFiles.get(i));
		}
		index = end;
		return r;
	}
}
