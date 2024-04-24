package com.gyoo.minilabo.Vecteurs;

/* index:
 * 0 ; 1 ; 2 ; 3
 * 4 ; 5 ; 6 ; 7
 * 8 ; 9 ; 10; 11
 * 12; 13; 14; 15
 *
 * use : x + y*4
 * */

public class Matrice4f {
    public float[] mat;

    public void set(float[] mat){
        if(mat.length == 16) {
            this.mat = mat;
        }else{
            System.err.println("Matrice4f.set() | float mat[] needs to be 16 in length");
        }
    }

    public Matrice4f(){
        mat = new float[16];
        faireIdentité();
    }

    public void faireIdentité(){
        mat = new float[]
                {1.0f, 0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 0.0f, 1.0f};
    }

    public Matrice4f avoirIdentité(){
        mat = new float[]
                {1.0f, 0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 0.0f, 1.0f};
        return this;
    }

    public static Vecteur3f MultiplierMV(Matrice4f m, Vecteur3f v){
        float[] vec = new float[]{v.x,v.y,v.z,1.0f};
        float[] res = new float[4];
        Matrix.multiplyMV(res,0,m.mat,0,vec,0);
        return new Vecteur3f(res[0]/res[3],res[1]/res[3],res[2]/res[3]);
    }

    public static Matrice4f MultiplierMM(Matrice4f m, Matrice4f mb){
        Matrice4f res = new Matrice4f();
        Matrix.multiplyMM(res.mat,0,m.mat,0,mb.mat,0);
        return res;
    }

    public void inverse(){
        Matrix.invertM(mat,0,mat,0);
    }

    public static Matrice4f inverse(Matrice4f mat){
        Matrice4f resMat = new Matrice4f();
        Matrix.invertM(resMat.mat,0,mat.mat,0);
        return resMat;
    }

    public void rotation(Vecteur3f rotation){
        Matrice4f rot = new Matrice4f();
        Matrix.rotateM(rot.mat,0,rotation.x,1.0f,0.0f,0.0f);
        Matrix.rotateM(rot.mat,0,rotation.y,0.0f,1.0f,0.0f);
        Matrix.rotateM(rot.mat,0,rotation.z,0.0f,0.0f,1.0f);
        this.mat = Matrice4f.MultiplierMM(rot,this).mat;
    }

    public void translation(Vecteur3f translation){
        Matrix.translateM(mat, 0, mat, 0, translation.x,translation.y,translation.z);
    }

    public void échelle(Vecteur3f scale){
        Matrix.scaleM(mat, 0, mat, 0, scale.x,scale.y,scale.z);
    }

    public Matrice4f copier(){
        Matrice4f r = new Matrice4f();
        for (int i = 0; i < mat.length; i++) {
            r.mat[i] = mat[i];
        }
        return r;
    }
}
