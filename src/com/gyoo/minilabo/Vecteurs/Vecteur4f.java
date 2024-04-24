package com.gyoo.minilabo.Vecteurs;

public class Vecteur4f {

    public float x;
    public float y;
    public float z;
    public float w;

    public Vecteur4f(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vecteur4f(float xyzw){
        this.x = xyzw;
        this.y = xyzw;
        this.z = xyzw;
        this.w = xyzw;
    }

    public void addi(Vecteur4f b){
        x += b.x;
        y += b.y;
        z += b.z;
        w += b.w;
    }

    public void sous(Vecteur4f b){
        x -= b.x;
        y -= b.y;
        z -= b.z;
        w -= b.w;
    }

    public void mult(float s){
        x *= s;
        y *= s;
        z *= s;
        w *= s;
    }

    public void mult(Vecteur4f m){
        x *= m.x;
        y *= m.y;
        z *= m.z;
        w *= m.w;
    }

    public void div(Vecteur4f d){
        x = x/d.x;
        y = y/d.y;
        z = z/d.z;
        w = w/d.w;
    }

    public void norm(){
        x = x/ longueur();
        y = y/ longueur();
        z = z/ longueur();
        w = w/ longueur();
    }

    public float longueur(){
        return (float) Math.sqrt(Math.pow(x,2.0)+Math.pow(y,2.0)+Math.pow(z,2.0)+Math.pow(w,2.0));
    }

    public Vecteur4f copier(){
        return new Vecteur4f(x,y,z,w);
    }

    public static Vecteur4f addi(Vecteur4f a, Vecteur4f b){
        return new Vecteur4f(a.x+b.x, a.y + b.y, a.z+b.z, a.w+b.w);
    }

    public static Vecteur4f sous(Vecteur4f a, Vecteur4f b){
        return new Vecteur4f(a.x-b.x, a.y - b.y, a.z-b.z, a.w-b.w);
    }

    public static Vecteur4f mult(Vecteur4f a, float s){
        return new Vecteur4f(a.x * s,a.y*s, a.z*s, a.w*s);
    }

    public static Vecteur4f mult(Vecteur4f a, Vecteur4f b){
        return new Vecteur4f(a.x*b.x,a.y*b.y, a.z*b.z, a.w*b.w);
    }

    public static Vecteur4f div(Vecteur4f a, Vecteur4f b){
        return new Vecteur4f(a.x/b.x,a.y/b.y, a.y/b.z, a.w/b.w);
    }

    public static float scal(Vecteur4f a, Vecteur4f b){
        return (a.x*b.x)+(a.y*b.y)+(a.z*b.z)+(a.w*b.w);
    }

    public static Vecteur4f norm(Vecteur4f a){
        return new Vecteur4f(a.x/a.longueur(),a.y/a.longueur(),a.z/a.longueur(), a.w/a.longueur());
    }

    public static float distance(Vecteur4f a, Vecteur4f b){
        return (float) Math.sqrt(Math.pow(a.x-b.x,2.0)+Math.pow(a.y-b.y,2.0)+Math.pow(a.z-b.z,2.0)+Math.pow(a.w-b.w,2.0));
    }

    public Vecteur4f opposé(){
        return new Vecteur4f(-x,-y,-z,-w);
    }
}
