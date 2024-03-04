#version 460
precision mediump float;

in vec2 uv;

uniform sampler2D image;

out vec4 Fragment;

void main(){
	Fragment = texture(image,uv);
}