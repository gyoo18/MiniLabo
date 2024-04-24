#version 460
precision mediump float;

in vec2 uv;

uniform vec2 ech;

const float r = 30.0;

out vec4 Fragment;

void main(){
	vec2 coord = (uv*2.0 - 1.0);
	float fac = float(max(abs(coord.x),abs(coord.y))>0.99);
	vec4 col = vec4(fac);
		
	Fragment = vec4(fac);
}