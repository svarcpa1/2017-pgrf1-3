import Model.Vertex;
import objectdata.*;
import raster.ZbufferAlgorithm;
import rasterdata.RasterImage;
import rasterdata.RasterImageImmutable;
import rasterdata.RasterImagePresenterAWT;
import rasterops.LineRasterizerDDA;
import renderer.RasterizerTriangles;
import transformops.Renderer;
import transformops.RendererWireframe;
import transforms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Function;

/**
 * trida pro kresleni na platno: zobrazeni pixelu
 *
 * @author PGRF FIM UHK; Pavel Švarc
 * @version 2017
 */
public class Canvas {

    //init parameters
	private final JFrame frame;
	private final JPanel panel;
	private final BufferedImage img;

	private RasterImage<Integer> rasterImage;
    private final LineRasterizerDDA<Integer> linerDDA;

    private final Mesh cube = new Cube();
	private final Mesh pyramid = new Pyramid();
	private final Mesh axes = new Axes();
/*    private Flat flat = new Flat(1);*/
	private final Renderer<Integer> render;

    private Mat4 persp, persp1;
    private final Mat4 transl;
	private Camera cam;
	private Mat4 viewMat;
	private Mat4 model = new Mat4Identity();
    private Mat4 model2 = new Mat4Identity();
	private Mat4 initialMatrix = new Mat4();
	//usage for axes
	private Mat4 initialMatrix2 = new Mat4();
	private int previousX, previousY;
	private int view=0;

	class ColorShader implements Function<Vertex, Col>{
		@Override
		public Col apply(Vertex vertex) {
			return vertex.getColor().mul(1/vertex.getOne());
		}
	}

	public Canvas(final int width, final int height) {

		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("UHK FIM PGRF2: " + this.getClass().getName()+ " Úkol 1. Pavel Švarc");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ZbufferAlgorithm zb = new ZbufferAlgorithm(img);

        RasterizerTriangles rt = new RasterizerTriangles(zb, new Function<Vertex, Col>() {
            @Override
            public Col apply(Vertex vertex) {
                return vertex.getColor().mul(1/vertex.getOne());
            }
        });

		/*
		rasterImage = new RasterImageBuffered<>(img,
				// toInteger: Function<PixelType, Integer>
				Function.identity()
				,
				// toPixelType: Function<Integer, PixelType>
				Function.identity());
		*/

        //immutable raster much slower presenter, but easy undo use (that's why moving in not fluent
        rasterImage = new RasterImageImmutable<>(width, height, 0);
		linerDDA = new LineRasterizerDDA<>();
		render = new RendererWireframe<>(linerDDA);

		cam = new Camera()
				.withPosition(new Vec3D(5,3,2))
				.withAzimuth(Math.PI)
				.withZenith(-Math.atan2(2, 5));

		viewMat = cam.getViewMatrix();

		persp =  new Mat4PerspRH(Math.PI / 3,
				(double)height / width, 0.1, 1000);

		persp1 = new Mat4OrthoRH(7.5, 7.5, 0.1, 1000);

		transl = new Mat4Transl(0.5,0,0);

		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				present(g);
			}
		};

		panel.setPreferredSize(new Dimension(width, height));

		panel.requestFocusInWindow();

		panel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int currentX = e.getX();
				int currentY = e.getY();
				double dx = previousX-currentX;
				double dy = previousY-currentY;

				//moving camera
				if(SwingUtilities.isLeftMouseButton(e)){
					System.out.println("left");
					cam = cam.addAzimuth(Math.PI * dx / (double)9000);
					cam = cam.addZenith(Math.PI * dy / (double)9000);

					viewMat=cam.getViewMatrix();

					draw();
					panel.repaint();
				}

				//rotation
				if(SwingUtilities.isRightMouseButton(e)) {
					model = model.mul(new Mat4RotZ(Math.PI * dx / (double)9000));
					model = model.mul(new Mat4RotY(Math.PI * dy / (double)9000));
					draw();
					panel.repaint();
				}
			}
		});

		//zooming (scale)
		panel.addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.getWheelRotation() < 0){
					model = model.mul(new Mat4Scale(0.75,0.75,0.75));
				}else{
					model = model.mul(new Mat4Scale(1.25,1.25,1.25));
				}
				draw();
				panel.repaint();
			}
		});

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				previousX=e.getX();
				previousY=e.getY();
			}
		});

		//key bindings
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				//camera - right
			    if (e.getKeyCode() == KeyEvent.VK_D) {
					cam = cam.right(0.2);
					viewMat = cam.getViewMatrix();
					draw();
					panel.repaint();
				}
                //camera - left
				if (e.getKeyCode() == KeyEvent.VK_A) {
					cam = cam.left(0.2);
                    viewMat = cam.getViewMatrix();
					draw();
					panel.repaint();
				}
                //camera - forward
				if (e.getKeyCode() == KeyEvent.VK_W) {
					cam = cam.forward(0.2);
                    viewMat = cam.getViewMatrix();
					draw();
					panel.repaint();
				}
                //camera - backward
				if (e.getKeyCode() == KeyEvent.VK_S) {
					cam = cam.backward(0.2);
                    viewMat = cam.getViewMatrix();
					draw();
					panel.repaint();
				}
                //camera - up
				if (e.getKeyCode() == KeyEvent.VK_R) {
					cam = cam.up(0.2);
                    viewMat = cam.getViewMatrix();
					draw();
					panel.repaint();
				}
                //camera - down
				if (e.getKeyCode() == KeyEvent.VK_F) {
					cam = cam.down(0.2);
                    viewMat = cam.getViewMatrix();
					draw();
					panel.repaint();
				}
				//wireframe translation - down
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					model = model.mul(new Mat4Transl(0,0,-0.2));
					draw();
					panel.repaint();
				}
                //wireframe translation - up
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					model = model.mul(new Mat4Transl(0,0,0.2));
					draw();
					panel.repaint();
				}
                //wireframe translation - left
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					model = model.mul(new Mat4Transl(0,-0.2,0));
					draw();
					panel.repaint();
				}
                //wireframe translation - right
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					model = model.mul(new Mat4Transl(0,0.2,0));
					draw();
					panel.repaint();
				}
                //wireframe translation - forward
				if (e.getKeyCode() == KeyEvent.VK_J) {
					model = model.mul(new Mat4Transl(0.2,0,0));
					draw();
					panel.repaint();
				}
                //wireframe translation - backward
				if (e.getKeyCode() == KeyEvent.VK_U) {
					model = model.mul(new Mat4Transl(-0.2,0,0));
					draw();
					panel.repaint();
				}
				//changing perspective
				if (e.getKeyCode() == KeyEvent.VK_P) {
					Mat4 tmp;

					tmp = persp;
					persp = persp1;
					persp1=tmp;

					draw();
					panel.repaint();
				}
				//changing view (with help of selectView method below)
                if (e.getKeyCode() == KeyEvent.VK_V) {
					if(view==0) view++;
				    view = view % 5;
					selectView(view);
                    draw();
                    panel.repaint();
                }
                //restarting scene
				if (e.getKeyCode() == KeyEvent.VK_C) {
					view=0;
					persp = new Mat4PerspRH(Math.PI / 3,
							(double)height / width, 0.1, 1000);

					cam = new Camera()
							.withPosition(new Vec3D(5,3,2))
							.withAzimuth(Math.PI)
							.withZenith(-Math.atan2(2, 5));

					viewMat=cam.getViewMatrix();
					draw();
					panel.repaint();
				}
			}
		});

		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

    /**
     * Method for clearing canvas
     */
	public void clear() {
		rasterImage = rasterImage.cleared(0x2f2f2f);
	}

    /**
     * Mathod for presenting on canvas
     * @param graphics
     */
	public void present(final Graphics graphics) {
        //much faster only for RasterImageBuffered
	    //graphics.drawImage(img, 0, 0, null);
	    new RasterImagePresenterAWT<>(rasterImage, Function.identity()).present(graphics);
	}

	Optional<RasterImage<Integer>> processVec2D(Vec2D vec2D) {
		return Optional.of(rasterImage.withPixel(
				(int) vec2D.getX(),
				(int) vec2D.getY(),
				0xffffff));
	}

    /**
     * method for drawing individual objects into rasterImage
     */
	public void draw() {
		clear();
		initialMatrix =model.mul(viewMat).mul(persp).mul(transl);
        initialMatrix2 =model2.mul(viewMat).mul(persp).mul(transl);

		//cube
		rasterImage =render.render(rasterImage,cube.getVertices(),cube.getIndices(),0xff0000, initialMatrix);
		//pyramid
        rasterImage =render.render(rasterImage,pyramid.getVertices(),pyramid.getIndices(),0xffff00,initialMatrix);
		//axes
		rasterImage =render.render(rasterImage,axes.getVertices(),axes.getIndices(),0xff00ff, initialMatrix2);
        //flat
/*		rasterImage =render.renderTriangle(rasterImage,flat.getReCountVertices(),flat.getIndices(),
                0x0000ff, initialMatrix);*/






	}

	public void start() {
		draw();
		panel.repaint();
	}

    /**
     * method for setting camera view
     * @param i parametter is a code for selecting
     */
	public void selectView(int i){
		if (i == 0 ) {
			viewMat = cam.getViewMatrix();
			view++;
		}else if (i ==1 ) {
			//view from upside (z)
			viewMat = new Mat4ViewRH(	new Vec3D(0.5, 0.5, 5.0),
										new Vec3D(0.5, 0.5, -5.0),
										new Vec3D(0.0, 0.0, -1.0));
			view++;
		}else if(i == 2) {
			//view from downside (z)
			viewMat = new Mat4ViewRH(	new Vec3D(0.5, 0.5, -5),
										new Vec3D(0.5, 0.5, 5.0),
										new Vec3D(0.0, 0.0, 0.0));
			view++;
		}else if(i == 3) {
			//view from x
			viewMat = new Mat4ViewRH(	new Vec3D(5.0, 0.5, 0.5),
										new Vec3D(0.0, 0.0, 0.0),
										new Vec3D(0.0, 0.0, 1.0));
			view++;
		}else if(i == 4) {
			//view from y
			viewMat = new Mat4ViewRH(	new Vec3D(0.5, 5.0, 0.5),
										new Vec3D(0.0, -1.0, 0.0),
										new Vec3D(0.0, 0.0, -1.0));
			view++;
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Canvas(800, 600)::start);
	}

}