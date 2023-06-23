package com.example.movieappbackend.domain.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.movieappbackend.api.dtos.dto.WatchedMovieDto;
import com.example.movieappbackend.domain.model.Genre;
import com.example.movieappbackend.domain.model.MovieListItem;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecommendationService {
	
	private final WatchedMovieService watchedMovieService;
	
	private final MovieListItemService movieListItemService;
	
	private final GenreService genreService;
	
	public List<MovieListItem> executarRecomendacao() {
		List<WatchedMovieDto> movieList = watchedMovieService.watchedMovies();
		List<MovieListItem> movieListResult = movieListItemService.findAll();
		int userRate;
		List<List<Double>> parametersList = new ArrayList<>();
		List<Integer> userRateList = new ArrayList<>();
		MovieListItem movie;
		for(WatchedMovieDto movieDto : movieList) {
			userRate = movieDto.getRate();
			movie = movieListResult.stream()
					.filter(movieListItem -> movieListItem.getImdbId().equals(movieDto.getImdbId()))
					.findFirst().orElse(null);
			parametersList.add( collectMovieParameters(movie) );
			userRateList.add(userRate);
		}
		MultipleLinearRegression model = trainModel(parametersList, userRateList);
		List<Double> result = moviesResult(model, movieListResult);
		
		Map<MovieListItem, Double> map = new HashMap<>();
		for(int i = 0; i < result.size(); i++) {
			map.put(movieListResult.get(i), result.get(i));
		}
		
		Comparator<Double> byNota = (Double nota1, Double nota2) -> nota1.compareTo(nota2);

		LinkedHashMap<MovieListItem, Double> sortedMap = map.entrySet().stream()
		                .sorted(Map.Entry.<MovieListItem, Double>comparingByValue(byNota))
		                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
        List<MovieListItem> sortedResult = new ArrayList<>();
        for (Map.Entry<MovieListItem, Double> entry : sortedMap.entrySet()) {
            sortedResult.add(entry.getKey());
        }
		
		return sortedResult;
		
	}
	
	public List<Double> collectMovieParameters(MovieListItem movie){
		List<String> nomeGeneros = genreService.findAll().stream()
				.map(genre -> genre.getName()).collect(Collectors.toList());
		
		List<String> genresNames = new ArrayList<String>();
		
		for(Genre genre : movie.getGenres()) {
			genresNames.add( genre.getName() );
		}
		
		List<Double> parametros = new ArrayList<Double>();
		for (int i = 0; i < 28; i++) { parametros.add(0.0); }
		int indice;
		for (String genero : genresNames){
		      indice = nomeGeneros.indexOf(genero);
		      parametros.set(indice, 1.0);
		    }
		
		double imdbRating = (double) movie.getImdbRating();
		parametros.add(imdbRating / 100);
		return parametros;
		
	}
	
	public MultipleLinearRegression trainModel(List<List<Double>> parametrosFilme, List<Integer> notasUsuario) {
		int nRows = parametrosFilme.size();
		int nColumns = parametrosFilme.get(0).size();
		
		double[][] novoX = new double[nRows+22][nColumns];
		double[] novoY = new double[nRows+22];
		for (int i = 0; i < nRows; i++) {
			novoY[i] = ((double) notasUsuario.get(i)) / 100;
			for(int j = 0; j < nColumns; j++) {
				novoX[i][j] = parametrosFilme.get(i).get(j);
			}
		}
		
		for(int i = 0; i < 22; i++) {
			for(int j = 0; j < 28; j++) {
				novoX[nRows+i][j] = (double) ((int) i / 11);
			}
			novoX[nRows+i][28] = (i%11) * 10;
		}
		
		for(int i = 0; i < 22; i++) {
			novoY[i+nRows] = (i%11) * 10;
		}

		
		MultipleLinearRegression regression = new MultipleLinearRegression(nColumns, 10000);
		regression.trainSGD(novoX, novoY);	
		return regression;
	}
	
	public List<Double> moviesResult(MultipleLinearRegression regression, List<MovieListItem> movieList) {
		List<List<Double>> parametrosFilme = new ArrayList<>();
		for(MovieListItem movie : movieList) {
			parametrosFilme.add( collectMovieParameters(movie) );
		}
		
		int nColumns = parametrosFilme.get(0).size();
		
	
		double[] parametros = new double[nColumns];
		List<Double> result = new ArrayList<>();
		double valor;
		for(List<Double> x : parametrosFilme) {
			for (int j = 0; j < nColumns; j++) {
				parametros[j] = x.get(j);
			}
			valor = 100 * regression.predict(parametros).get();
			result.add(valor);
		}
		 
		 return result;
		
	}
	
}
