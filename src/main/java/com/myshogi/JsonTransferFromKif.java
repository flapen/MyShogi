package com.myshogi;

import java.util.regex.Pattern;
import org.json.*;

public class JsonTransferFromKif {
	
	private String json_kifu;
	private String kif_kifu;
	
	/* コンストラクタ */
	public JsonTransferFromKif(String kif_kifu) {
		this.kif_kifu = kif_kifu;
	}

	/* json_kifu Getter */
	public String getJson_kifu() {
		return json_kifu;
	}

	/* json_kifu Setter */
	public void setJson_kifu(String json_kifu) {
		this.json_kifu = json_kifu;
	}

	/* kif_kifu Getter */
	public String getKif_kifu() {
		return kif_kifu;
	}

	/* kif_kifu Setter */
	public void setKif_kifu(String kif_kifu) {
		this.kif_kifu = kif_kifu;
	}
	
	/* kif形式からjson形式に変換 */
	public void transferKifToJson() {
		try {
			/* 改行コード統一 */
			String kif_str = this.kif_kifu.replaceAll("\r\n", "\n");
			kif_str = kif_str.replaceAll("\r", "\n");
			/* ヘッダとボディに分割 */
			String kif_str_header = "";
			String kif_str_body = "";
			String[] strs = kif_str.split("\n");
			boolean headerFlg = true;
			for (int i = 0;i < strs.length; i++) {
				if (strs[i].substring(0, 2).equals("手数")) headerFlg = false;
				else if (headerFlg) kif_str_header = kif_str_header + strs[i] + "\n";
				else kif_str_body = kif_str_body + strs[i] + "\n";
			}
			/* ヘッダ部分各種文字列置換 */
			kif_str_header = kif_str_header.replaceAll("開始日時", "StartDate");
			kif_str_header = kif_str_header.replaceAll("棋戦", "MatchField");
			kif_str_header = kif_str_header.replaceAll("持ち時間", "AllottedTime");
			kif_str_header = kif_str_header.replaceAll("手合割", "MatchCondition");
			kif_str_header = kif_str_header.replaceAll("先手", "Black");
			kif_str_header = kif_str_header.replaceAll("後手", "White");
			kif_str_header = kif_str_header.replaceAll("：", ":");
			/* ボディ部分各種文字列置換 */
			kif_str_body = kif_str_body.replaceAll("一", "1");
			kif_str_body = kif_str_body.replaceAll("二", "2");
			kif_str_body = kif_str_body.replaceAll("三", "3");
			kif_str_body = kif_str_body.replaceAll("四", "4");
			kif_str_body = kif_str_body.replaceAll("五", "5");
			kif_str_body = kif_str_body.replaceAll("六", "6");
			kif_str_body = kif_str_body.replaceAll("七", "7");
			kif_str_body = kif_str_body.replaceAll("八", "8");
			kif_str_body = kif_str_body.replaceAll("九", "9");
			kif_str_body = kif_str_body.replaceAll("投了", "00");
			kif_str_body = kif_str_body.replaceAll("同 ", "01");
			kif_str_body = kif_str_body.replaceAll("同  ", "01");
			kif_str_body = kif_str_body.replaceAll("同　", "01");
			kif_str_body = kif_str_body.replaceAll("打", "(00)");
			kif_str_body = kif_str_body.replaceAll("王", "01");
			kif_str_body = kif_str_body.replaceAll("玉", "02");
			kif_str_body = kif_str_body.replaceAll("金", "03");
			kif_str_body = kif_str_body.replaceAll("銀", "04");
			kif_str_body = kif_str_body.replaceAll("桂", "05");
			kif_str_body = kif_str_body.replaceAll("香", "06");
			kif_str_body = kif_str_body.replaceAll("歩", "07");
			kif_str_body = kif_str_body.replaceAll("飛", "08");
			kif_str_body = kif_str_body.replaceAll("角", "09");
			kif_str_body = kif_str_body.replaceAll("成銀", "10");
			kif_str_body = kif_str_body.replaceAll("成桂", "11");
			kif_str_body = kif_str_body.replaceAll("成香", "12");
			kif_str_body = kif_str_body.replaceAll("と", "13");
			kif_str_body = kif_str_body.replaceAll("龍", "14");
			kif_str_body = kif_str_body.replaceAll("馬", "15");
			kif_str_body = kif_str_body.replaceAll("成", "E");
			kif_str_body = kif_str_body.replaceAll("\\(", " ");
			kif_str_body = kif_str_body.replaceAll("\\)", " ");
			kif_str_body = kif_str_body.replaceAll(":", " ");
			kif_str_body = kif_str_body.replaceAll("/", " ");
			/* json形式への変換処理(ヘッダ部分) */
			JSONObject jsonObj = new JSONObject();
			String key;
			String value;
			String[] strs_header = kif_str_header.split("\n");
			for (int i = 0; i < strs_header.length; i++) {
				key = ""; value = "";
				key = strs_header[i].split(":")[0];
				value = strs_header[i].split(":")[1];
				for (int j = 2; j < strs_header[i].split(":").length; j++) {
					value = value + ":" + strs_header[i].split(":")[j];
				}
				if (key.equals("AllottedTime")) {
					jsonObj.put(key, value);
					JSONObject allocattedTime = new JSONObject();
					int totalTime = 0;
					int pointer1 = 0;
					int pointer2 = 0;
					Pattern p1 = Pattern.compile("[0-9]");
					for (int j = 0; j < value.length(); j++) {
						if (p1.matcher(value.substring(j,j + 1)).find()) pointer2++;
						else if (value.substring(j, j + 1).equals("時")) {
							totalTime = totalTime + Integer.parseInt(value.substring(pointer1,pointer2)) * 3600;
							pointer2++;
							pointer1 = pointer2;
						} else if (value.substring(j, j + 1).equals("間")) { pointer1++; pointer2++;
						} else if (value.substring(j, j + 1).equals("分")) {
							totalTime = totalTime + Integer.parseInt(value.substring(pointer1,pointer2)) * 60;
							pointer2++;
							pointer1 = pointer2;
						} else if (value.substring(j, j + 1).equals("秒")) {
							totalTime = totalTime + Integer.parseInt(value.substring(pointer1,pointer2));
							pointer2++;
							pointer1 = pointer2;
						} else { pointer1++; pointer2++; 
						}
					}
					allocattedTime.put("TotalTime", totalTime);
					allocattedTime.put("CountdownTime", 0);
					allocattedTime.put("IgnoredTime", 0);
					allocattedTime.put("MaxOneTime", 0);
					jsonObj.put(key, allocattedTime);
				} else {
					jsonObj.put(key, value);
				}
			}
			/* json形式への変換処理(ボディ部分) */
			String[] strs_body = kif_str_body.split("\n");
			JSONObject move[] = new JSONObject[strs_body.length];
			int PreMoveX = 0;
			int PreMoveY = 0;
			for (int i = 0; i < strs_body.length; i++) {
				move[i] = new JSONObject();
				move[i].put("Count", Integer.parseInt(strs_body[i].split("[\\s]+")[0]));
				if (Integer.parseInt(strs_body[i].split("[\\s]+")[1].substring(0, 1)) == 0 && Integer.parseInt(strs_body[i].split("[\\s]+")[1].substring(1, 2)) == 0) {
					move[i].put("PieceType", 99);
					break;
				} else if (Integer.parseInt(strs_body[i].split("[\\s]+")[1].substring(0, 1)) == 0 && Integer.parseInt(strs_body[i].split("[\\s]+")[1].substring(1, 2)) == 1) {
					move[i].put("AfterPosX", PreMoveX);
					move[i].put("AfterPosY", PreMoveY);
				} else {
					move[i].put("AfterPosX", Integer.parseInt(strs_body[i].split("[\\s]+")[1].substring(0, 1)));
					move[i].put("AfterPosY", Integer.parseInt(strs_body[i].split("[\\s]+")[1].substring(1, 2)));
					PreMoveX = Integer.parseInt(strs_body[i].split("[\\s]+")[1].substring(0, 1));
					PreMoveY = Integer.parseInt(strs_body[i].split("[\\s]+")[1].substring(1, 2));
				}
				move[i].put("PieceType", Integer.parseInt(strs_body[i].split("[\\s]+")[1].substring(2, 4)));
				if (strs_body[i].split("[\\s]+")[1].length() == 5) move[i].put("Evolution", true);
				move[i].put("BeforePosX", Integer.parseInt(strs_body[i].split("[\\s]+")[2].substring(0, 1)));
				move[i].put("BeforePosY", Integer.parseInt(strs_body[i].split("[\\s]+")[2].substring(1, 2)));
				move[i].put("LostTime", ((Integer.parseInt(strs_body[i].split("[\\s]+")[3]) * 60) + Integer.parseInt(strs_body[i].split("[\\s]+")[4])));
			}
			jsonObj.put("Move", move);
			/* json文字列を取得 */
			this.json_kifu = jsonObj.toString();
		} catch (Exception e) {
			this.json_kifu = "kif形式フォーマットエラー";
		}
	}
}
