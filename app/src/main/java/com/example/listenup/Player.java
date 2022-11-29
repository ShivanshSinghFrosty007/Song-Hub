package com.example.listenup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {

    String type, name, image, song, position, itemCount;

    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf, btRepeat, btRepeatOn, btShuffle, btShuffleOn;

    ImageView songImage;
    TextView songName;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    int pos, repeatCheck = 0, shuffleCheck = 0, number = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        type = getIntent().getStringExtra("type");
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        song = getIntent().getStringExtra("song");
        position = getIntent().getStringExtra("id");
        itemCount = getIntent().getStringExtra("itemCount");

        pos = Integer.parseInt(position);

        songImage = findViewById(R.id.songImage);
        songName = findViewById(R.id.songname);

        playerPosition = findViewById(R.id.position);
        playerDuration = findViewById(R.id.duration);
        seekBar = findViewById(R.id.seekbar);
        btFf = findViewById(R.id.bt_ff);
        btPause = findViewById(R.id.bt_pause);
        btPlay = findViewById(R.id.bt_play);
        btRew = findViewById(R.id.bt_rew);
        btRepeat = findViewById(R.id.bt_repeat);
        btRepeatOn = findViewById(R.id.bt_repeat_on);
        btShuffle = findViewById(R.id.bt_shuffle);
        btShuffleOn = findViewById(R.id.bt_shuffleOn);

        imageChange(position);
        setSongName(position);

        mediaPlayer = MediaPlayer.create(this, Uri.parse(song));

        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };

        int duration = mediaPlayer.getDuration();
        String sDuration = Convert(duration);
        playerDuration.setText(sDuration);

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay.setVisibility(View.GONE);
                btPause.setVisibility(View.VISIBLE);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                handler.postDelayed(runnable, 0);
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPause.setVisibility(View.GONE);
                btPlay.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
            }
        });

        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(0);
                playerPosition.setText("00:00");
                changeSong(String.valueOf(++pos));
                imageChange(String.valueOf(pos));
                setSongName(String.valueOf(pos));
            }
        });

        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos > 0) {
                    playerPosition.setText("00:00");
                    changeSong(String.valueOf(--pos));
                    imageChange(String.valueOf(pos));
                    setSongName(String.valueOf(pos));
                } else
                    Toast.makeText(getApplicationContext(), "Nope", Toast.LENGTH_SHORT).show();
            }
        });

        btRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffleCheck == 0) {
                    if (repeatCheck == 0) {
                        repeatCheck = 1;
                        btRepeat.setVisibility(View.GONE);
                        btRepeatOn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btRepeatOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatCheck == 1) {
                    repeatCheck = 0;
                    btRepeatOn.setVisibility(View.GONE);
                    btRepeat.setVisibility(View.VISIBLE);
                }
            }
        });

        btShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffleCheck == 0) {
                    shuffleCheck = 1;
                    btShuffle.setVisibility(View.GONE);
                    btShuffleOn.setVisibility(View.VISIBLE);
                }
                if (repeatCheck == 1) {
                    repeatCheck = 0;
                    btRepeatOn.setVisibility(View.GONE);
                    btRepeat.setVisibility(View.VISIBLE);
                }
            }
        });

        btShuffleOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffleCheck == 1) {
                    shuffleCheck = 0;
                    btShuffle.setVisibility(View.VISIBLE);
                    btShuffleOn.setVisibility(View.GONE);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                playerPosition.setText(Convert(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        onCompleteSong();
    }

    @SuppressLint("DefaultLocale")
    private String Convert(int duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

    private void changeSong(String position) {
        btPause.setVisibility(View.GONE);
        btPlay.setVisibility(View.VISIBLE);
        mediaPlayer.stop();
        mediaPlayer.release();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(type).child(position).child("song");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(value));
                int duration = mediaPlayer.getDuration();
                String sDuration = Convert(duration);
                playerDuration.setText(sDuration);
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                if (shuffleCheck == 1) {
                    btPlay.setVisibility(View.GONE);
                    btPause.setVisibility(View.VISIBLE);
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration());
                }
                onCompleteSong();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void imageChange(String position) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(type).child(position).child("image");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Glide.with(getApplicationContext()).load(value).into(songImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSongName(String position) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(type).child(position).child("name");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                songName.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Random(int max) {
        double rand = Math.random() * max;
        if (rand == pos) {
            pos = (int) rand + 1;
            if (pos == max) {
                pos = 0;
            }
        } else {
            pos = (int) rand;
        }
    }

    private void onCompleteSong() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (shuffleCheck == 0) {
                    if (repeatCheck == 0) {
                        btPause.setVisibility(View.GONE);
                        btPlay.setVisibility(View.VISIBLE);
                        mediaPlayer.seekTo(0);
                    } else if (repeatCheck == 1) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                    }
                } else {
                    Random(Integer.parseInt(itemCount) - 1);
//                    seekBar.setProgress(0);
                    playerPosition.setText("00:00");
                    changeSong(String.valueOf(pos));
                    imageChange(String.valueOf(pos));
                    setSongName(String.valueOf(pos));

                }
            }
        });
    }
}