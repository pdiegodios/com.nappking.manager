package com.nappking.movietimesup.adapter;

import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.android.friendsmash.R;
import com.nappking.movietimesup.entities.Movie;

public class MovieListAdapter extends ArrayAdapter<Movie>{
	
	private List<Movie> _movies;
	private Context _context;
    
    public MovieListAdapter(Context context, int textViewResourceId, List<Movie> movies) {
        super(context, textViewResourceId, movies);
        this._movies = movies;
        this._context = context;
    }
    
    public List<Movie> getList(){
    	return _movies;
    }
    
    /**
     * cargamos los iconos en la vista seg�n las caracter�sticas de la tarea para su 
     * correcta visualizaci�n. Adem�s, se establecen las diferentes acciones sobre 
     * elementos del movieitem.xml
     * @param v : Vista sobre la que se cargan las propiedades de la tarea
     * @param movie : Pel�cula visualizada
     */
    private void display(View v, final Movie movie){
    	ImageView iMovie = (ImageView) v.findViewById(R.id.movieButton);
    	ImageView iForeground = (ImageView) v.findViewById(R.id.foreground);
    	TextView txTitle = (TextView) v.findViewById(R.id.title);
    	TextView txId = (TextView) v.findViewById(R.id.movieid);
    	TextView txPoints = (TextView) v.findViewById(R.id.moviepoints);
    	TextView txStarPoints = (TextView) v.findViewById(R.id.starmoviepoints);
        
    	if (iMovie != null) {
    		txId.setText(String.format("%04d",movie.getId()));
    		txPoints.setText(String.format("%04d",movie.getPoints()));
    		txStarPoints.setText(movie.getPoints()+"");
    		RotateAnimation ranim = (RotateAnimation)AnimationUtils.loadAnimation(_context, R.anim.animrotate);
    		ranim.setFillAfter(true);
    		txId.setAnimation(ranim);
    		txPoints.setAnimation(ranim);
    		txStarPoints.setVisibility(View.INVISIBLE);
        	if(movie.isLocked(this._context)) {
        		//Movie was locked and you can see anything about that
        		iMovie.setImageResource(R.drawable.ticket);
        		iForeground.setImageResource(R.drawable.lock);
        		iForeground.setVisibility(View.VISIBLE);
        		txTitle.setVisibility(View.INVISIBLE);
        		v.setAlpha((float) 0.6);
        	}	
            else if (movie.isUnlocked(this._context)){
            	//Movie was unlocked so you can see the poster and see the specific data
        		iMovie.setImageResource(R.drawable.ticket);
        		iForeground.setVisibility(View.INVISIBLE);
        		txTitle.setVisibility(View.VISIBLE);
        		txTitle.setText(movie.getTitle());
        		txStarPoints.setVisibility(View.VISIBLE);
        		v.setAlpha((float) 1);
            }
            else{
            	//Movie is ready to play
            	iMovie.setImageResource(R.drawable.ticket);
            	iForeground.setImageResource(R.drawable.question);
            	iForeground.setVisibility(View.VISIBLE);
            	txTitle.setVisibility(View.INVISIBLE);
            	v.setAlpha((float) 1);
            }
        	
        	/*
        	iMovie.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(movie.isUnlocked(v.getContext())){
						//showFilmRecord(movie);
					}	
					else if(movie.isUnlocked(v.getContext())){
						//startGame(movie);
					}
				}
			});*/
        	
    	}        
    }  
    
    /*
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    	ImageView bmImage;

    	public DownloadImageTask(ImageView bmImage) {
    		this.bmImage = bmImage;
    	}

    	protected Bitmap doInBackground(String... urls) {
    		String urldisplay = urls[0];
    		Bitmap mIcon11 = null;
    		try {
    			InputStream in = new java.net.URL(urldisplay).openStream();
    			mIcon11 = BitmapFactory.decodeStream(in);
    		} catch (Exception e) {
    			Log.e("Error", e.getMessage());
    			e.printStackTrace();
    		}
    		return mIcon11;
    	}

    	protected void onPostExecute(Bitmap result) {
    		bmImage.setImageBitmap(result);
    	}
    	
    }
    */

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layout = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layout.inflate(R.layout.movieitem, null);
        }
        Movie movie = (Movie) _movies.get(position);
        if (movie != null) {
        	display(view,movie);	            
        }
        return view;
    }    

}
