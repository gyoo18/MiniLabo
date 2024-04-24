#version 460
precision mediump float;

in vec2 uv;

uniform vec2 ech;

const float r = 30.0;

out vec4 Fragment;

void main(){
	vec2 coord = uv*2.0 - 1.0;
	float m = length(coord);
	vec3 col = mix( vec3(0.2,0.2,0.22), vec3(0.18,0.18,0.2), m);
	
	coord = abs(uv*2.0 - 1.0)*ech;
	vec2 cp = ech-r;
	float mi = min((coord-cp).x,(coord-cp).y);
	m = float( ( distance(coord,cp) < r && mi > 0.0) || mi < 0.0);
	col = mix(vec3(0.1,0.1,0.1),col,m);
		
	Fragment = vec4(col,1.0);
}