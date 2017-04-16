package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import main.rooms.ManToilet;

public class TestEnv extends jason.environment.Environment {

	private Logger logger = Logger.getLogger("testenv.mas2j."
			+ TestEnv.class.getName());
	
	Map<String,Integer> toiletmap=new HashMap<String,Integer>();
	View view;
	WorldModel wm;

	/** Called before the MAS execution with the args informed in .mas2j */
	@Override
	public void init(String[] args) {
		wm = new WorldModel();
		
		int count = 1;

		List<ManToilet> manToilets = wm.getManToiletListE();
		
		for (int i = 0; i < manToilets.size(); ++i) {
			toiletmap.put("IE"+String.valueOf(i-2)+"10",count);
			count++;
		}
		
		manToilets = wm.getManToiletListL();
		
		for (int i = 0; i < manToilets.size(); ++i) {
			toiletmap.put("IE"+String.valueOf(i-2)+"08",count);
			count++;
		}
		
		manToilets = wm.getManToiletListB();
		
		for (int i = 0; i < manToilets.size(); ++i) {
			toiletmap.put("IB"+String.valueOf(i-2)+"04",count);
			count++;
		}
		
		view = new View(wm, "WCCounter", 350, this);
	}

	@Override
	public boolean executeAction(String agName, Structure action) {
		if (action.getFunctor().equals("burn")) {
			addPercept("person", Literal.parseLiteral("change"));
			return true;
		}

		if (action.getFunctor().equals("sensordetect")) {
			addPercept("mantoiletsensor1", Literal.parseLiteral("takenWc"));
			return true;
		}

		return false;
	}
	
	private ManToilet getManToiletByParam(String param) {
		int count=toiletmap.get(param);
		
		if (count <= 7) {
			return wm.getManToiletListE().get(count-1);
		} else if (count <= 14) {
			return wm.getManToiletListL().get(count-8);
		} else {
			return wm.getManToiletListB().get(count-15);
		}
	}

	public void ManToiletTaken(String param) {
		ManToilet mt = getManToiletByParam(param);
		
		mt.decToilet();
		
		view.setTextOfManToilet(String.valueOf(mt.getToilet()));
	}
	
	public void ManToiletFree(String param) {
		ManToilet mt = getManToiletByParam(param);
		
		mt.incToilet();
		
		view.setTextOfManToilet(String.valueOf(mt.getToilet()));
	}
	
	public void ManUrinalTaken(String param) {
		ManToilet mt = getManToiletByParam(param);
		
		mt.decUrine();
		
		view.setTextOfManUrinal(String.valueOf(mt.getUrine()));
	}
	
	public void ManUrinalFree(String param) {
		ManToilet mt = getManToiletByParam(param);
		
		mt.incUrine();
		
		view.setTextOfManUrinal(String.valueOf(mt.getUrine()));
	}

	public String getManToilet(String param) {
		ManToilet mt = getManToiletByParam(param);
		
		return String.valueOf(mt.getToilet());
	}
	
	public String getManUrine(String param) {
		ManToilet mt = getManToiletByParam(param);
		
		return String.valueOf(mt.getUrine());
	}
	
	public String[] getRooms() {
		Set<String> s = toiletmap.keySet();
		
		String[] rooms = new String[s.size()];
		int i = 0;
		
		for (String a : s) {
			rooms[i++] = a;
		}
		
		return rooms;
	}

	/** Called before the end of MAS execution */
	@Override
	public void stop() {
		super.stop();
	}


}