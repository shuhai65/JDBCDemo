package main;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;


//link : https://blog.csdn.net/weixin_44960315/article/details/107325591

//测试基本的双线性对
public class JDBCDemo {

    public static void main(String[] args) {
        //生成Pairing示例
        //a.properties文件中存的是椭圆曲线参数
//        Pairing bp = PairingFactory.getPairing("a.properties");

        //自定义生成椭圆曲线参数
        //http://gas.dia.unisa.it/projects/jpbc/docs/ecpg.html#TypeA
        int rBits = 160;
        int qBits = 512;

// JPBC Type A pairing generator...
        TypeACurveGenerator pg = new TypeACurveGenerator(rBits, qBits);
        PairingParameters pp = pg.generate();
        System.out.println(pp);
        Pairing bp = PairingFactory.getPairing(pp);

        //生成群
        Field G1 = bp.getG1();
        Field Zr = bp.getZr();

        //生成元素
        Element g = G1.newRandomElement();
        Element a = Zr.newRandomElement();
        Element b = Zr.newRandomElement();

        //要使用duplicate()方法，不然会导致原输入值改变为输出值
        //或者在生成时使用Element g = G1.newRandomElement().getImmutable();使g的值不可变
        // 三、计算等式左半部分
        Element g_a = g.duplicate().powZn(a);
        Element g_b = g.duplicate().powZn(b);
        Element egg_ab = bp.pairing(g_a, g_b);

        // 四、计算等式右半部分
        Element egg = bp.pairing(g, g);
        Element ab = a.duplicate().mul(b);
        Element egg_ab_p = egg.duplicate().powZn(ab);

        if(egg_ab.isEqual(egg_ab_p)) {
            System.out.println("双线性对成立");
        }else {
            System.out.println("双线性对不成立");
        }
    }
}
