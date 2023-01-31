package stand.mc.login;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class LoginCmd implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if(args.length>0){
            String password =  args[0];//获取参数中的密码
            String playername = player.getName();
            String host = player.getAddress().getHostString();
            if(Login.Loginyml.writeyml(playername,host,password)){
                player.sendMessage(Login.head + ChatColor.WHITE + "登录成功!!!\n请牢记宁的密码："
                        + ChatColor.RED + password);
                return true;
            }else {
                player.sendMessage(Login.head+ChatColor.RED+"密码错误!!!");
                return false;
            }
        }
        player.sendMessage(Login.head+ChatColor.WHITE+"错误，缺少："+ChatColor.RED+"密码");
        return false;
    }
    //指令补全提示信息
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        LinkedList<String> tips = new LinkedList<>();
        if(args.length == 1) {//如果只输入了一个空格
            tips.add("密码");
        }
        return tips;
    }
}
