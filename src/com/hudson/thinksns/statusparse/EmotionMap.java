package com.hudson.thinksns.statusparse;

import java.util.HashMap;
import java.util.Map;

import com.hudson.thinksns.R;
/**
 * 
 * @author  Ø÷Ÿ≤≈
 * 
 */
public class EmotionMap {
	private static Map<String, Integer> EmMapS = new HashMap<String, Integer>();
	private static Map<Integer, String> EmMapI = new HashMap<Integer, String>();
	public static int[] imgemotion = { R.drawable.aoman, R.drawable.baiyan,
			R.drawable.bishi, R.drawable.bizui, R.drawable.cahan,
			R.drawable.caidao, R.drawable.chajin, R.drawable.cheer,
			R.drawable.chong, R.drawable.ciya, R.drawable.da,
			R.drawable.dabian, R.drawable.dabing, R.drawable.dajiao,
			R.drawable.daku, R.drawable.dangao, R.drawable.danu,
			R.drawable.dao, R.drawable.deyi, R.drawable.diaoxie, R.drawable.e,
			R.drawable.fadai, R.drawable.fadou, R.drawable.fan,
			R.drawable.fanu, R.drawable.feiwen, R.drawable.fendou,
			R.drawable.gangga, R.drawable.geili, R.drawable.gouyin,
			R.drawable.guzhang, R.drawable.haha, R.drawable.haixiu,
			R.drawable.haqian, R.drawable.hua, R.drawable.huaixiao,
			R.drawable.hufen, R.drawable.huishou, R.drawable.huitou,
			R.drawable.jidong, R.drawable.jingkong, R.drawable.jingya,
			R.drawable.kafei, R.drawable.keai, R.drawable.kelian,
			R.drawable.ketou, R.drawable.kiss, R.drawable.ku,

			R.drawable.kuaikule, R.drawable.kulou, R.drawable.kun,
			R.drawable.lanqiu, R.drawable.lenghan, R.drawable.liuhan,
			R.drawable.liulei, R.drawable.liwu, R.drawable.love, R.drawable.ma,
			R.drawable.meng, R.drawable.nanguo, R.drawable.no, R.drawable.ok,
			R.drawable.peifu, R.drawable.pijiu, R.drawable.pingpang,
			R.drawable.pizui, R.drawable.qiang, R.drawable.qinqin,
			R.drawable.qioudale, R.drawable.qiu, R.drawable.quantou,
			R.drawable.ruo, R.drawable.se, R.drawable.shandian,
			R.drawable.shengli, R.drawable.shenma, R.drawable.shuai,
			R.drawable.shuijiao, R.drawable.taiyang, R.drawable.tiao,
			R.drawable.tiaopi, R.drawable.tiaosheng, R.drawable.tiaowu,
			R.drawable.touxiao, R.drawable.tu, R.drawable.tuzi,
			R.drawable.wabi, R.drawable.weiqu, R.drawable.weixiao,
			R.drawable.wen, R.drawable.woshou, R.drawable.xia,
			R.drawable.xianwen, R.drawable.xigua, R.drawable.xinsui,
			R.drawable.xu,

			R.drawable.yinxian, R.drawable.yongbao, R.drawable.youhengheng,
			R.drawable.youtaiji, R.drawable.yueliang, R.drawable.yun,
			R.drawable.zaijian, R.drawable.zhadan, R.drawable.zhemo,
			R.drawable.zhuakuang, R.drawable.zhuanquan, R.drawable.zhutou,
			R.drawable.zuohengheng, R.drawable.zuotaiji, R.drawable.zuqiu };
	public static String[] wenziemotion = { "[aoman]", "[baiyan]", "[bishi]",
			"[bizui]", "[cahan]", "[caidao]", "[chajin]", "[cheer]", "[chong]",
			"[ciya]", "[da]", "[dabian]", "[dabing]", "[dajiao]", "[daku]",
			"[dangao]", "[danu]", "[dao]", "[deyi]", "[diaoxie]", "[e]",
			"[fadai]", "[fadou]", "[fan]", "[fanu]", "[feiwen]", "[fendou]",
			"[gangga]", "[geili]", "[gouyin]", "[guzhang]", "[haha]",
			"[haixiu]", "[haqian]", "[hua]", "[huaixiao]", "[hufen]",
			"[huishou]", "[huitou]", "[jidong]", "[jingkong]", "[jingya]",
			"[kafei]", "[keai]", "[kelian]", "[ketou]", "[kiss]", "[ku]",

			"[kuaikule]", "[kulou]", "[kun]", "[lanqiu]", "[lenghan]",
			"[liuhan]", "[liulei]", "[liwu]", "[love]", "[ma]", "[meng]",
			"[nanguo]", "[no]", "[ok]", "[peifu]", "[pijiu]", "[pingpang]",
			"[pizui]", "[qiang]", "[qinqin]", "[qioudale]", "[qiu]",
			"[quantou]", "[ruo]", "[se]", "[shandian]", "[shengli]",
			"[shenma]", "[shuai]", "[shuijiao]", "[taiyang]", "[tiao]",
			"[tiaopi]", "[tiaosheng]", "[tiaowu]", "[touxiao]", "[tu]",
			"[tuzi]", "[wabi]", "[weiqu]", "[weixiao]", "[wen]", "[woshou]",
			"[xia]", "[xianwen]", "[xigua]", "[xinsui]", "[xu]",

			"[yinxian]", "[yongbao]", "[youhengheng]", "[youtaiji]",
			"[yueliang]", "[yun]", "[zaijian]", "[zhadan]", "[zhemo]",
			"[zhuakuang]", "[zhuanquan]", "[zhutou]", "[zuohengheng]",
			"[zuotaiji]", "[zuqiu]"

	};

	public static Map<String, Integer> getEmMapS() {

		for (int i = 0; i < imgemotion.length; i++) {
			EmMapS.put(wenziemotion[i], imgemotion[i]);
		}

		return EmMapS;
	}

	public static Map<Integer, String> getEmMapI() {
		for (int i = 0; i < imgemotion.length; i++) {
			EmMapI.put(imgemotion[i], wenziemotion[i]);
		}
		return EmMapI;
	}

}
