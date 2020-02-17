import java.lang.String;
import java.util.List;
import java.util.ArrayList;
class InfectStatistic {
    public static void main(String[] args) 
    {
        //用来保存路径以及日期
        String log_Path,out_Path,date;
        log_Path="";
        out_Path="";
        date="";
        List type,province;
        type=new ArrayList();
        province=new ArrayList();
        if(args.length==0||!args[0].equals("list"))
        {
            System.out.println("您输入的命令有误，请检查后重新输入");
        }
        else
        {
            for(int i=1;i<args.length;++i)
            {
                if(args[i].equals("-log"))
                {
                    log_Path=args[++i];
                }
                else if(args[i].equals("-out"))
                {
                    out_Path=args[++i];
                }
                else if(args[i].equals("-date"))
                {
                    date=args[++i];
                }
                else if(args[i].equals("-type"))
                {
                    for(++i;i<args.length;++i)
                    {
                        if(!args[i].equals("-province"))
                        {
                        type.add(args[i]);
                        }
                        else 
                        {
                            --i;
                            break;
                        }
                    }
                }
                else if(args[i].equals("-province"))
                {
                    for(++i;i<args.length;++i)
                    {
                        if(!args[i].equals("-type"))
                        {
                        province.add(args[i]);
                        }
                        else 
                        {
                            --i;
                            break;
                        }
                    }
                }
            }
            System.out.println("读取日志的路径为"+log_Path);
            System.out.println("输出日志的路径为"+out_Path);
            System.out.println("读取日志的日期为"+date);
            System.out.println("需要统计的患者类型为"+type+type.size());
            System.out.println("需要统计的地区为"+province+type.size());
        }
    }
}
