package plugins.matrix.heatmap;

import java.io.PrintWriter;



public class RequestHandler {
	
	public static void handle(MatrixHeatmapModel screenModel, String action, PrintWriter out) throws Exception {
		if (action.equals("download_visible")) {
			screenModel.getBrowser().getModel().getSubMatrix().writeToCsvWriter(out);
			//FIXME: close 'out'?
		}
		else if (action.equals("download_all")) {
			screenModel.getBrowser().getModel().getInstance().writeToCsvWriter(out);
			//FIXME: close 'out'?
		}
		else if(action.equals("removeCustomScale")){
			screenModel.setAutoScale(true);
		}
		else if (action.equals("refresh")) {
			screenModel.setSelectedData(null);
		}
		else if (action.equals("changeSubmatrixSize")) {
			screenModel.getBrowser().update();
		}
		else if (action.equals("moveRight")) {
			screenModel.getBrowser().moveRight();
		}
		else if (action.equals("moveLeft")) {
			screenModel.getBrowser().moveLeft();
		}
		else if (action.equals("moveDown")) {
			screenModel.getBrowser().moveDown();
		}
		else if (action.equals("moveUp")) {
			screenModel.getBrowser().moveUp();
		}
		else if (action.equals("moveFarRight")) {
			screenModel.getBrowser().moveFarRight();
		}
		else if (action.equals("moveFarLeft")) {
			screenModel.getBrowser().moveFarLeft();
		}
		else if (action.equals("moveFarDown")) {
			screenModel.getBrowser().moveFarDown();
		}
		else if (action.equals("moveFarUp")) {
			screenModel.getBrowser().moveFarUp();
		}else{
			throw new Exception("Action '"+action+"' unknown.");
		}
	}

}
