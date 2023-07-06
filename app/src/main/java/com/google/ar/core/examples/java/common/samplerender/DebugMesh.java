package com.google.ar.core.examples.java.common.samplerender;

import com.google.ar.core.Pose;

import java.io.IOException;

public class DebugMesh {
    public Shader shader;
    public IndexBuffer indexBufferObject;
    public VertexBuffer vertexBufferObject;
    private static final int COORDS_PER_VERTEX = 3;
    public Mesh mesh;
    public DebugMesh(SampleRender render)throws IOException{
        Texture texture = Text.drawCanvasToTexture(render,"Draw Me",24);
        shader = Shader.createFromAssets(
                                render,
                                "shaders/ar_unlit_object.vert",
                                "shaders/ar_unlit_object.frag",
                                null)
                        .setTexture("u_Texture",texture);
        indexBufferObject = new IndexBuffer(render,null);
        vertexBufferObject = new VertexBuffer(render,COORDS_PER_VERTEX,null);
        VertexBuffer[] vertexBuffers = {vertexBufferObject};
        mesh = Mesh.createFromAsset(render,"models/cube.obj");
    }
    public void drawMesh(SampleRender render, Pose cameraPose){
        render.draw(mesh,shader);
    }
}
