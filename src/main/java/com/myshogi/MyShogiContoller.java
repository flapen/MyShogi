package com.myshogi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

@Controller
public class MyShogiContoller {
	/* トップページ */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		return mav;
	}
	
	/* kif形式棋譜入力 */
	@RequestMapping(value="/", method=RequestMethod.POST)
	public ModelAndView send(@RequestParam("kif_text")String str, ModelAndView mav) {
		JsonTransferFromKif jtfk = new JsonTransferFromKif(str);
		jtfk.transferKifToJson();
		mav.addObject("json_kifu", jtfk.getJson_kifu());
		mav.setViewName("index");
		return mav;
	}
}
