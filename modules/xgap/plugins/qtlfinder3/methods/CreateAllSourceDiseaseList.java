package plugins.qtlfinder3.methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAllSourceDiseaseList
{
	
	public static Map<String, List<String>> createAllSourceDiseaseList(List<String> list){

		Map<String, List<String>> sourcesAndDiseases = new HashMap<String, List<String>>();
		
		String disease = new String();
		String source = new String();
		String[] split = null;
		
		for(String diseaseInput : list){

			split = diseaseInput.toString().split("\\|\\|\\%\\|\\|");
			
			disease = split[0].toString();
			source = split[1].toString();								

			if(sourcesAndDiseases.keySet().contains(source)){
				sourcesAndDiseases.get(source).add(disease);
			}else{
				List<String> diseaseList = new ArrayList<String>();
				diseaseList.add(disease);
				sourcesAndDiseases.put(source, diseaseList);
			}	
		}
		
		return sourcesAndDiseases;	
	}
}
