package com.gyoo.minilabo.Vecteurs;

public class Vecteur3f {
    public float x;
    public float y;
    public float z;

    public Vecteur3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vecteur3f(Vecteur3f copy){
        this.x = copy.x;
        this.y = copy.y;
        this.z = copy.z;
    }

    public Vecteur3f(float xyz){
        this.x = xyz;
        this.y = xyz;
        this.z = x;
    }

    public void addi(Vecteur3f b){
        x += b.x;
        y += b.y;
        z += b.z;
    }

    public void sous(Vecteur3f b){
        x -= b.x;
        y -= b.y;
        z -= b.z;
    }

    public void mult(float s){
        x *= s;
        y *= s;
        z *= s;
    }

    public void mult(Vecteur3f m){
        x *= m.x;
        y *= m.y;
        z *= m.z;
    }

    public void div(Vecteur3f d){
        x = x/d.x;
        y = y/d.y;
        z = z/d.z;
    }

    public void norm(){
        x = x/ longueur();
        y = y/ longueur();
        z = z/ longueur();
    }

    public float longueur(){
        return (float) Math.sqrt(x*x+y*y+z*z);
    }

    public Vecteur3f vec(Vecteur3f c){
        Vecteur3f r = new Vecteur3f(0,0,0);
        r.x = (y*c.z)-(z*c.y);
        r.y = (x*c.z)-(z*c.x);
        r.z = (x*c.y)-(y*c.x);
        return r;
    }

    public static float distance(Vecteur3f a, Vecteur3f b){
        return (float) Math.sqrt((a.x-b.x) * (a.x-b.x) + (a.y-b.y) * (a.y-b.y) + (a.z-b.z) * (a.z-b.z));
    }

    public static Vecteur3f addi(Vecteur3f a, Vecteur3f b){
        return new Vecteur3f(a.x+b.x, a.y + b.y, a.z+b.z);
    }

    public static Vecteur3f sous(Vecteur3f a, Vecteur3f b){
        return new Vecteur3f(a.x-b.x, a.y - b.y, a.z-b.z);
    }

    public static Vecteur3f mult(Vecteur3f a, float s){
        return new Vecteur3f(a.x * s,a.y*s, a.z*s);
    }

    public static Vecteur3f mult(Vecteur3f a, Vecteur3f b){
        return new Vecteur3f(a.x*b.x,a.y*b.y, a.z*b.z);
    }

    public static Vecteur3f div(Vecteur3f a, Vecteur3f b){
        return new Vecteur3f(a.x/b.x,a.y/b.y, a.y/b.z);
    }

    public static float scal(Vecteur3f a, Vecteur3f b){
        return (a.x*b.x)+(a.y*b.y)+(a.z*b.z);
    }

    public static Vecteur3f norm(Vecteur3f a){
        return new Vecteur3f(a.x/a.longueur(),a.y/a.longueur(),a.z/a.longueur());
    }

    public static Vecteur3f dirigerVers(Vecteur3f positionInitiale, Vecteur3f destination, float assiette){
        Vecteur3f a = Vecteur3f.sous(positionInitiale,destination);
        Vecteur3f b = new Vecteur3f(0,0,0);
        Vecteur3f c = Vecteur3f.norm(a);
        b.x = (float)Math.toDegrees(Math.asin(-c.y));
        b.y = (float)Math.toDegrees(Math.atan2(a.x,a.z));
        b.z = assiette;
        return b;
    }

    public static Vecteur3f vec(Vecteur3f a, Vecteur3f b){
        Vecteur3f r = new Vecteur3f(0,0,0);
        r.x = (a.y*b.z)-(a.z*b.y);
        r.y = (a.x*b.z)-(a.z*b.x);
        r.z = (a.x*b.y)-(a.y*b.x);
        return r;
    }

    public Vecteur3f copier(){
        return new Vecteur3f(x,y,z);
    }

    public Vecteur3f opposé(){
        return new Vecteur3f(-x,-y,-z);
    }

    public static long vecteurEnLong(float x, float y, float z){
        return ( (((long)Float.floatToIntBits(x)&0xFFFFF800)<<32)+(((long)Float.floatToIntBits(y)&0xFFFFF800)<<11)+(((long)Float.floatToIntBits(z)&0x003FFFFF)>>10) );
    }

    public static Vecteur3f longEnVecteur(long v){
        float x = Float.intBitsToFloat((int) v<<10 &0x003FFFFF);
        float y = Float.intBitsToFloat((int) v>>11 &0xFFFFF800);
        float z = Float.intBitsToFloat((int) v>>32 &0xFFFFF800);
        return new Vecteur3f(x,y,z);
    }

}
