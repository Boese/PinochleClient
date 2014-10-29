package com.edu.pinochleclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import naga.NIOService;
import naga.NIOSocket;
import naga.SocketObserver;
import naga.packetreader.AsciiLinePacketReader;
import naga.packetwriter.AsciiLinePacketWriter;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleLayoutGameActivity;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.edu.pinochleenum.Card;
import com.edu.pinochleenum.Request;
import com.edu.pinochleenum.Suit;
import com.edu.pinochlescene.MenuScene;
import com.edu.utils.PlayerResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlayActivity extends SimpleLayoutGameActivity{
	// ===========================================================
		// Constants
		// ===========================================================

		private static final int CAMERA_WIDTH = 720;
		private static final int CAMERA_HEIGHT = 800;

		// ===========================================================
		// Fields
		// ===========================================================

		private EditText mEditText;
		private TextView mConsoleText;
		private ScrollView mScrollView;

		private Button sendButton;
		
		private NIOService service;
		private NIOSocket socket;
		
		private MessageHandler msgHandler;
		
		private static Request request = Request.Null;
		private static PlayerResponse response = new PlayerResponse();
		private static List<Card> cards = new ArrayList<Card>();
		private static ObjectMapper mapper = new ObjectMapper();
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK)
				try{
					socket.closeAfterWrite();
					msgHandler.cancel(true);
				}catch(Exception e) {}
		      System.exit(0);
			return false;
		}
		
		@Override
		protected int getLayoutID() {
			return R.layout.pinochleclienttextbased;
		}

		@Override
		protected int getRenderSurfaceViewID() {
			return R.id.pinochleclient_rendersurfaceview;
		}

		@Override
		protected void onSetContentView() {
			super.onSetContentView();

			this.mEditText = (EditText)this.findViewById(R.id.editText1);
			this.mConsoleText = (TextView)this.findViewById(R.id.textView1);
			this.mScrollView = (ScrollView) this.findViewById(R.id.scrollView1);
			this.mEditText.requestFocus();
			
			this.sendButton = (Button) findViewById(R.id.button1);
			this.sendButton.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	try{
	                	response = new PlayerResponse();
	                	int x;
	                		switch(request) {
	                		case Card:
	                			x = Integer.parseInt(mEditText.getText().toString());
	                			response.setCard(cards.get(x));
	                			break;
							case Bid:
								x = Integer.parseInt(mEditText.getText().toString());
								response.setBid(x);
								break;
							case Cards: 
								List<Card> tempCards = new ArrayList<Card>();
								int a = Integer.parseInt(mEditText.getText().toString());
								int b = Integer.parseInt(mEditText.getText().toString());
								int c = Integer.parseInt(mEditText.getText().toString());
								int d = Integer.parseInt(mEditText.getText().toString());
								a++;b++;c++;d++;
								tempCards.add(cards.get(a));
								tempCards.add(cards.get(b));
								tempCards.add(cards.get(c));
								tempCards.add(cards.get(d));
								response.setCards(tempCards);
								break;
							case Null:
								break;
							case Trump: 
								x = Integer.parseInt(mEditText.getText().toString());
								x--;
								Suit trump = Suit.Hearts;
								response.setTrump(trump.getNext(x));
								break;
							default:
								break;
	                	}
	                		socket.write(mapper.writeValueAsBytes(response));
	                }
	                catch(Exception e) {
	                	e.printStackTrace();
	                }
			    	appendMessage(mEditText.getText().toString());
			    	mEditText.setText("");
			    }
			});
		}

		@Override
		public EngineOptions onCreateEngineOptions() {
			final Camera camera = new Camera(0, 0, PlayActivity.CAMERA_WIDTH, PlayActivity.CAMERA_HEIGHT);

			return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
					new RatioResolutionPolicy(PlayActivity.CAMERA_WIDTH, 
							PlayActivity.CAMERA_HEIGHT), camera);
		}

		@Override
		public void onCreateResources() {
			msgHandler = new MessageHandler();
		}

		@Override
		public Scene onCreateScene() {
			final Scene scene = new Scene();
			scene.getBackground().setColor(Color.WHITE);
			
			msgHandler.execute();
			
			return scene;
		}
		
		private void appendMessage(final String msg) {
			mScrollView.post(new Runnable() {
				@Override
				public void run() {
					mConsoleText.append("]: " + msg + System.getProperty("line.separator"));
					mScrollView.fullScroll(View.FOCUS_DOWN);
					mEditText.requestFocus();
				}
			});
			
		}
		
		private class MessageHandler extends AsyncTask<Void,Void,Void> {
			public MessageHandler() {}
			
			@Override
			protected Void doInBackground(Void... params) {
				try
		        {
		                // Start up the service.
		                service = new NIOService();
		                
		                // Open our socket.
		                socket = service.openSocket("10.0.2.2", 5217);
		                
		                // Use regular 1 byte header reader/writer
		                socket.setPacketReader(new AsciiLinePacketReader());
		                socket.setPacketWriter(new AsciiLinePacketWriter());
		                
		                // Start listening to the socket.
		                socket.listen(new SocketObserver()
		                {
		                	@Override
		                    public void connectionOpened(NIOSocket nioSocket)
		                    {
		                		appendMessage("connection opened");
		                    }
		                	@Override
				            public void packetSent(NIOSocket socket, Object tag)
				            {
		                		appendMessage("packet sent");
				            }
		                	@Override
			            	public void packetReceived(NIOSocket socket, byte[] packet)
		                    {
		                		try
		                        {
		                            String message = new String(packet).trim();
		                                try {
		                                	JSONObject j = new JSONObject(message);
		                                	request = Request.valueOf(j.optString("currentRequest"));
		                                	if(!cards.containsAll((Collection<?>) mapper.readValue(j.optString("cards"), new TypeReference<List<Card>>() { }))) {
		                                		cards = mapper.readValue(j.optString("cards"), new TypeReference<List<Card>>() { });
		                                		appendMessage("Cards : " + cards);
		                                	}
		                                	switch(request) { 
					                		case Card: appendMessage("Enter Card (1-4):");
					                			break;
											case Bid: appendMessage("Enter Bid:");
												break;
											case Cards: appendMessage("Enter Cards (1-12),(1-12),(1-12),(1-12):");
												break;
											case Null: appendMessage(j.optString("currentMessage"));
												break;
											case Trump: appendMessage("Enter Trump (1-4):");
												break;
											default:
												break;
		                                	}
		                                }
		                                catch(Exception e) {
		                                	appendMessage("NotJson: " + (message));
		                                	e.printStackTrace();
		                                }
		                        }
		                        catch (Exception e)
		                        {
		                        	appendMessage(e.toString());
		                        }
		                		
		                    }
		                	@Override
		                    public void connectionBroken(NIOSocket nioSocket, Exception exception)
		                    {
		                		appendMessage("Connection failed.");
		                    }
		                });
		                
		            // Read IO until process exits.
		            while(true) {
		            	service.selectBlocking();
		            	//Thread.sleep(100);
		            }
		            
		        }
		        catch (Exception e)
		        {
		        	appendMessage(e.toString());
		        }
				return null;
			}
			
		}
	}