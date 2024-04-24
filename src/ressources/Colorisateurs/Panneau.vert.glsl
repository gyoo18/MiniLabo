#version 460
precision mediump float;

in vec2 pos;

uniform vec2 trans;
uniform vec2 ech;
uniform vec2 res;

out vec2 uv;

void main(){
	vec2 posFin = (pos*(ech*2.0) + (trans*2.0))/res;
	uv = pos*0.5 + 0.5;
	gl_Position = vec4(posFin,-0.5,1.0);
}