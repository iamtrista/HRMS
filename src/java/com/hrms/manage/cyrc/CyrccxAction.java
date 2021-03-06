package com.hrms.manage.cyrc;

import com.hrms.table.Dwlb;
import com.hrms.table.Dwxz;
import com.hrms.table.Gzdw;
import com.hrms.table.Sshy;
import com.hrms.table.Ssxt;
import com.hrms.table.Xl;
import com.hrms.table.Xw;
import com.hrms.table.Yh;
import com.hrms.util.ComboboxUtil;
import com.hrms.util.Util;
import com.jplus.json.EasyUiJson;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jplus.hyb.database.DatabaseINI;
import org.jplus.hyb.database.Hyberbin;
import org.jplus.hyb.servlet.ServletUtil;
import org.jplus.yydbgai.DatabaseAccess;
import org.jplus.yydbgai.EasyMapsManager;

/**
 *
 * @author hp
 */
@WebServlet(name = "CyrccxAction", urlPatterns = {"/manage/tsrcxx/cyrc/CyrccxAction.jsp"})
public class CyrccxAction extends HttpServlet {

    /**
     * 下面是模式关键字 可以自行删除和增加自定义模式，关键字一定要大写 默认模式为OTHER=0,所以OTHER不能删除
     */
    public final static int OTHER = 0;//其它
    public final static int SHOWLIST = 1;//显示列表
    public final static int GETTABLES = 2;//查询得到列表
    public final static int SHOWONE = 3;//个体详情
//    public final static int GETGZDW = 4;
    public final static int BAIDUINPUT = 5;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        excute(ServletUtil.setModel(request.getParameter("mode"), this), request, response);
    }

    /**
     * 主执行方法
     *
     * @param event 方法ID
     */
    private void excute(int event, HttpServletRequest request, HttpServletResponse response) {
        switch (event) {
            case OTHER:
                try {
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                } catch (ServletException ex) {
                    Logger.getLogger(CyrccxAction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CyrccxAction.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case SHOWLIST:
                showList(request, response);
                break;
            case GETTABLES:
                getTables(request, response);
                break;
            case SHOWONE:
                showOne(request, response);
                break;
            case BAIDUINPUT:
                getDwmcs(request, response);
                break;
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * 显示列表
     */
    private void showOne(HttpServletRequest request, HttpServletResponse response) {
        DatabaseAccess dao = null;
        try {
            int cyrcid = 1;
            try {
                cyrcid = Integer.parseInt(request.getParameter("cyrcid"));
            } catch (Exception e) {
                e.printStackTrace();
                cyrcid = 1;
            }
            dao = new DatabaseAccess();
            EasyMapsManager emm = new EasyMapsManager(dao);
            String jbxxsql = "SELECT  `zzmm`.`zzmmmc`, `cyrc`.`yrzjg`,   `cyrc`.`yzw`,   `cyrc`.`zhgzsj`,   `cyrc`.`email`,   `cyrc`.`yjzdz`,   `cyrc`.`zyry`,   `jbxx`.`zjhm`,  `jbxx`.`gzdwid`,  `jbxx`.`xm`,   `jbxx`.`xb`, `jbxx`.`zp`, `jbxx`.`hjszd`, `jbxx`.`csrq`,   `jbxx`.`mz`,   `jbxx`.`jg`,   `gzdw`.`dwmc`,   `jl`.`xxjl`,   `jl`.`gzjl`,   `jl`.`jcqk`,   `xw`.`xwmc`,   `xl`.`xlmc`,   `jbxx`.`gzsj`,   `jbxx`.`rdsj`,   `jbxx`.`zw`,   `zj`.`zjmc`,   `jbxx`.`lxdh`,   `jbxx`.`txdz`,   `jbxx`.`jkzk`,   `jbxx`.`hyzkbm`,   `jbxx`.`zymc`,   `jbxx`.`byxx`,   `jbxx`.`yzbm` FROM   `jbxx`  LEFT  JOIN `cyrc` ON `cyrc`.`ryid` = `jbxx`.`ryid`  LEFT  JOIN `gzdw` ON `jbxx`.`gzdwid` = `gzdw`.`gzdwid`   LEFT JOIN `jl` ON `cyrc`.`cyrcid` = `jl`.`jlid`  LEFT  JOIN `xw` ON `jbxx`.`xwbm` = `xw`.`xwbm`   LEFT JOIN `xl` ON `jbxx`.`xlbm` = `xl`.`xlbm` LEFT  JOIN `zzmm` ON `jbxx`.`zzmmbm` = `zzmm`.`zzmmbm` LEFT  JOIN `zj` ON `jbxx`.`zjbm` = `zj`.`zjbm` where `jbxx`.`ryid`=?;";
            emm.setPreparedParameter(cyrcid);
            List list = emm.executeQuery(jbxxsql);
            request.setAttribute("list", list);
            request.getRequestDispatcher("/manage/tsrcxx/cyrccx/cyrcxq.jsp").forward(request, response);

        } catch (ServletException ex) {
            Logger.getLogger(CyrccxAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CyrccxAction.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dao.close();
        }
    }

    private void getTables(HttpServletRequest request, HttpServletResponse response) {
        Connection conn = DatabaseINI.getDatabase().getConn();
        Hyberbin hyb = null;
        hyb = new Hyberbin(new Dwxz(), conn);
        List dwxzlist = hyb.showAll();
        hyb = new Hyberbin(new Dwlb(), conn);
        List dwlblist = hyb.showAll();
        hyb = new Hyberbin(new Ssxt(), conn);
        List ssxtlist = hyb.showAll();
        hyb = new Hyberbin(new Sshy(), conn);
        List gzdwlist = hyb.showAll();
        hyb = new Hyberbin(new Xl(), conn);
        List xllist = hyb.showAll();
        hyb = new Hyberbin(new Xw(), conn);
        List xwlist = hyb.showAll();
        hyb.reallyClose();
        String json = ComboboxUtil.createBatchComboJSON(dwxzlist, ssxtlist, gzdwlist, xllist, xwlist, dwlblist);
        ServletUtil.ajaxData(json, response);
    }

    private void showList(HttpServletRequest request, HttpServletResponse response) {
        DatabaseAccess dao = new DatabaseAccess();
        try {
            EasyMapsManager emm = new EasyMapsManager(dao);
            EasyUiJson datagrid = new EasyUiJson(request);
            int year = new Date(System.currentTimeMillis()).getYear() + 1900;
            int nl1 = 0;
            int nl2 = 100;
            String sql = "SELECT  `jbxx`.`ryid`, `jbxx`.`xm`,   `jbxx`.`xb`,   `gzdw`.`dwmc`,   `xl`.`xlmc`,   `jbxx`.`csrq`,   `jbxx`.`zjhm` FROM   `cyrc`  left JOIN `jbxx` ON `cyrc`.`ryid` = `jbxx`.`ryid` left JOIN `xl` ON `xl`.`xlbm` = `jbxx`.`xlbm`   left JOIN `gzdw` ON `jbxx`.`gzdwid` = `gzdw`.`gzdwid` where ";
            String xlys = request.getParameter("xlys");
            String xwys = request.getParameter("xwys");
            String xltj = xlys.equals("true") ? "<=" : "=";
            String xwtj = xwys.equals("true") ? "<=" : "=";
            String dwxzbm = request.getParameter("dwxz");
            String xwbm = request.getParameter("xw");
            String xlbm = request.getParameter("xl");
            String dwlbbm = request.getParameter("dwlb");
            String ssxtbm = request.getParameter("ssxt");
            String sshybm = request.getParameter("sshy");
            try {
                nl1 = Integer.parseInt(request.getParameter("nl1"));
                nl1 = nl1 < 0 ? 0 : nl1;
            } catch (Exception e) {
                nl1 = 0;
            }
            try {
                nl2 = Integer.parseInt(request.getParameter("nl2"));
                nl2 = nl2 < 0 ? 0 : nl2;
            } catch (Exception e) {
                nl2 = 100;
            }
            String where = " (" + year + "-year(csrq)) between ? and ?";
            emm.setPreparedParameter(nl1);
            emm.setPreparedParameter(nl2);
            Yh yh = (Yh) request.getSession().getAttribute("user");
            String jb = yh.getJb();
            where += jb.equals("1") ? "" : jb.equals("2") ? " and `gzdw`.`gzdwid`=" + yh.getGzdwid() + " or `gzdw`.`sjdwid`=" + yh.getGzdwid() : " and `gzdw`.`gzdwid`=" + yh.getGzdwid();
            if (!Util.isEmpty(xwbm)) {
                where = where + " and xwbm" + xwtj + "?";
                emm.setPreparedParameter(xwbm);
            }
            if (!Util.isEmpty(xlbm)) {
                where = where + " and `jbxx`.`xlbm`" + xltj + "?";
                emm.setPreparedParameter(xlbm);
            }
            String dwmc = request.getParameter("gzdw");
            if (!(Util.isEmpty(dwmc))) {
                where = where + " and `gzdw`.`dwmc` = ? ";
                emm.setPreparedParameter(dwmc);
            } else {
                if (!Util.isEmpty(dwxzbm)) {
                    where = where + " and dwxzbm=?";
                    emm.setPreparedParameter(dwxzbm);
                }
                if (!Util.isEmpty(dwlbbm)) {
                    where = where + " and dwlbbm=?";
                    emm.setPreparedParameter(dwlbbm);
                }
                if (!Util.isEmpty(ssxtbm)) {
                    where = where + " and ssxtbm=?";
                    emm.setPreparedParameter(ssxtbm);
                }
                if (!Util.isEmpty(sshybm)) {
                    where = where + " and sshybm=? ";
                    emm.setPreparedParameter(sshybm);
                }
            }
            sql += where;
            List list = emm.executeQuery(sql, datagrid);
            datagrid.putAll(list);
            ServletUtil.ajaxData(datagrid.toDataString(), response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询信息不正确");
        } finally {
            dao.close();
        }
    }

    private void getDwmcs(HttpServletRequest request, HttpServletResponse response) {
        DatabaseAccess dao = new DatabaseAccess();
        EasyMapsManager emm = new EasyMapsManager(dao);
        String dwmcpy = request.getParameter("input_id");
        System.out.println(dwmcpy);
        String json = "";
        String where = "";
        if (!Util.isEmpty(dwmcpy)) {
            emm.setPreparedParameter(dwmcpy + "%");
            String sql = "select dwmc from gzdw where hypy like ?";
            Yh yh = (Yh) request.getSession().getAttribute("user");
            String jb = yh.getJb();
            where += jb.equals("1") ? "" : jb.equals("2") ? " and `gzdw`.`gzdwid`=" + yh.getGzdwid() + " or `gzdw`.`sjdwid`=" + yh.getGzdwid() : " and `gzdw`.`gzdwid`=" + yh.getGzdwid();
            ArrayList<HashMap> list = emm.executeQuery(sql + where);
            json = "[";
            for (int i = 0; i < list.size(); i++) {
                json += "\"" + list.get(i).get("dwmc") + "\"" + ",";
            }
            json = json.substring(0, json.length() - 1 > 0 ? json.length() - 1 : 1);
            json += "]";
        }
        dao.close();
        ServletUtil.ajaxData(json, response);
    }
}
