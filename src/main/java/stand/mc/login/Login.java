package stand.mc.login;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public final class Login extends JavaPlugin {
    public static String head = ChatColor.YELLOW+"[login-stand插件]";
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new MyListener(), this);//注册监听器
        this.getCommand("login").setExecutor(new LoginCmd());
        Loginyml.start();//初始化文件
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN+"[Login-stand]重新加载");//广播
    }
    //监听类
    public class MyListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            String host = player.getAddress().getHostString();//获取ip
            String playerName = player.getName();
            int readCode = Loginyml.readyml(playerName, host);//获取注册登录情况
            switch (readCode){
                case 0:
                    player.sendMessage(head+ChatColor.WHITE+"检测到宁未注册：请输入"+ChatColor.RED+"/login 密码");
                    break;
                case 1:
                    player.sendMessage(head+ChatColor.WHITE+"自动登录成功");
                    Loginyml.writeyml(playerName,"1","null");//禁止移动
                    break;
                case 2:
                    player.sendMessage(head+ChatColor.WHITE+"检测到宁ip变动：请输入"+ChatColor.RED+"/login 密码");
                    break;
                case 3:
                    player.sendMessage(ChatColor.RED+"[Login-stand]插件异常");
                    break;
            }
            return;
        }
        @EventHandler//退出登录
        public void onPlayerQuit(PlayerQuitEvent event) {
            Player player = event.getPlayer();
            String host = player.getAddress().getHostString();//获取ip
            String playerName = player.getName();
            int readCode = Loginyml.readyml(playerName, host);//获取注册登录情况
            if(readCode==0){
                return;
            }
            Loginyml.writeyml(playerName,"0","null");
        }
        @EventHandler
        public void onPlayerMove(PlayerMoveEvent event) {
            Player player = event.getPlayer();
            //获取登录状态
            if(Loginyml.readPlayerCode(player.getName())){
                return;
            }
            event.setCancelled(true);
            player.sendMessage(head+ChatColor.WHITE+"请输入"+ChatColor.RED+"/login 密码");
        }
    }
    //yml相关
    public static class Loginyml{
        public static String ymlpath = "plugins/login.yml";

        private static boolean readPlayerCode(String playername){
            File f = new File(ymlpath);
            Yaml yaml = new Yaml();
            Map map = null,playermap= null;
            try {
                map = yaml.load(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                return false;
            }
            playermap = (Map) map.get(playername);
            if(playermap==null){
                return false;
            }else {
                String code = playermap.get("code").toString();
                if(! code.equals("1")){
                    return false;
                }
            }
            return true;
        }
        /**
         * 判断玩家是否注册
         * @param playername
         * @param host
         * @return 0：未注册 1：成功 2：异地登录 3.异常
         */
        private static int readyml(String playername,String host){
            File f = new File(ymlpath);
            Yaml yaml = new Yaml();
            Map map = null,playermap= null;
            try {
                map = yaml.load(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                return 3;
            }
            playermap = (Map) map.get(playername);
            if(playermap==null){//未创建
                return 0;
            }else{
                String oldhost = playermap.get("host").toString();
                if(! oldhost.equals(host)){//异地登录
                    return 2;
                }
            }
            return 1;
        }

        /**
         * 写入密码和判断密码是否正确
         * @param playername
         * @param host 1：登录成功 0：退出
         * @param password
         * @return true：密码正确/设置成功 false:密码错误/设置失败
         */
        public static boolean writeyml(String playername,String host, String password){
            File f = new File(ymlpath);
            Yaml yaml = new Yaml();
            Map map = null;
            try {
                map = yaml.load(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            Map playermap = null;
            playermap = (Map) map.get(playername);
            if(playermap==null){
                playermap = new HashMap();
                playermap.put("host",host);
                playermap.put("password",password);
                playermap.put("code",1);
            }else {
                if(host.equals("1")){//登录之后
                    playermap.put("code",1);
                }else if(host.equals("0")){//退出服务器
                    playermap.put("code",0);
                }else{
                    String oldpass = playermap.get("password").toString();
                    if(password.equals(oldpass)){//如果密码正确
                        playermap.put("host",host);
                        playermap.put("code",1);
                    }else {
                        return false;
                    }
                }
            }
            map.put(playername,playermap);
            Writer f_w = null;
            try {
                f_w = new FileWriter(ymlpath);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            yaml.dump(map, f_w);
            return true;
        }
        /**
         *判断login文件是否存在，不存在就创建
         */
        private static void start(){
            File f = new File(ymlpath);
            if(!f.exists()) {
                try {
                    f.createNewFile();
                    HashMap map = new HashMap();
                    map.put("server", "初始化");
                    Writer f_w = new FileWriter(ymlpath);
                    Yaml yaml = new Yaml();
                    yaml.dump(map, f_w);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
