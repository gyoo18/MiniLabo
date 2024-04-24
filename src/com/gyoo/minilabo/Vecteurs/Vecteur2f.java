package com.gyoo.minilabo.Vecteurs;

public class Vecteur2f {
    public float x;
    public float y;

    public Vecteur2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vecteur2f(float xy){
        this.x = xy;
        this.y = xy;
    }

    public void addi(Vecteur2f b){
        x += b.x;
        y += b.y;
    }

    public void sous(Vecteur2f b){
        x -= b.x;
        y -= b.y;
    }

    public void mult(float s){
        x *= s;
        y *= s;
    }

    public void mult(Vecteur2f m){
        x *= m.x;
        y *= m.y;
    }

    public void div(Vecteur2f d){
        x = x/d.x;
        y = y/d.y;
    }

    public void norm(){
        x = x/ longueur();
        y = y/ longueur();
    }

    public static Vecteur2f addi(Vecteur2f a, Vecteur2f b){
        return new Vecteur2f(a.x+b.x, a.y + b.y);
    }

    public static Vecteur2f sous(Vecteur2f a, Vecteur2f b){
        return new Vecteur2f(a.x-b.x, a.y - b.y);
    }

    public static Vecteur2f mult(Vecteur2f a, float s){
        return new Vecteur2f(a.x * s,a.y*s);
    }

    public static Vecteur2f mult(Vecteur2f a, Vecteur2f b){
        return new Vecteur2f(a.x*b.x,a.y*b.y);
    }

    public static Vecteur2f div(Vecteur2f a, Vecteur2f b){
        return new Vecteur2f(a.x/b.x,a.y/b.y);
    }

    public float longueur(){
        return (float) Math.sqrt(Math.pow(x,2.0)+Math.pow(y,2.0));
    }

    public static float distance(Vecteur2f a, Vecteur2f b){
        return (float) Math.sqrt(Math.pow(a.x-b.x,2.0)+Math.pow(a.y-b.y,2.0));
    }

    public static float scal(Vecteur2f a, Vecteur2f b){
        return (a.x*b.x)+(a.y*b.y);
    }

    public static Vecteur2f norm(Vecteur2f a){
        return new Vecteur2f(a.x/a.longueur(),a.y/a.longueur());
    }

    public Vecteur2f copier(){
        return new Vecteur2f(x,y);
    }

    public Vecteur2f opposé(){
        return new Vecteur2f(-x,-y);
    }
}
